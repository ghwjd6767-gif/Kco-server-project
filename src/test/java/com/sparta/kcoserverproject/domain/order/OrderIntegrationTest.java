package com.sparta.kcoserverproject.domain.order;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sparta.kcoserverproject.domain.order.dto.OrderRequestDto;
import com.sparta.kcoserverproject.domain.order.repository.OrderRepository;
import com.sparta.kcoserverproject.domain.product.entity.Product;
import com.sparta.kcoserverproject.domain.product.repository.ProductRepository;
import com.sparta.kcoserverproject.domain.user.entity.User;
import com.sparta.kcoserverproject.domain.user.repository.UserRepository;
import com.sparta.kcoserverproject.global.lock.service.RedisLockService;
import com.sparta.kcoserverproject.infrastructure.kafka.event.OrderCompletedEvent;
import com.sparta.kcoserverproject.infrastructure.kafka.producer.OrderEventProducer;
import com.sparta.kcoserverproject.infrastructure.redis.repository.RankingRedisRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class OrderIntegrationTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    UserRepository userRepository;

    @Autowired
    ProductRepository productRepository;

    @Autowired
    OrderRepository orderRepository;

    @MockitoBean
    RedisLockService redisLockService;

    @MockitoBean
    OrderEventProducer orderEventProducer;

    @MockitoBean
    RankingRedisRepository rankingRedisRepository;

    @Test
    void 주문_API_요청시_포인트가_차감되고_주문이_생성되며_Kafka와_Ranking이_호출된다() throws Exception {

        // given
        User user = userRepository.save(
                User.builder()
                        .point(10_000L)
                        .build()
        );

        Product product = productRepository.save(
                Product.builder()
                        .name("아메리카노")
                        .price(BigDecimal.valueOf(3000))
                        .build()
        );

        OrderRequestDto request = new OrderRequestDto(
                user.getId(),
                product.getId(),
                1L
        );

        when(redisLockService.tryLock(anyString(), anyString(), anyLong()))
                .thenReturn(true);

        // when & then
        mockMvc.perform(post("/api/orders")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userId").value(user.getId()))
                .andExpect(jsonPath("$.productId").value(product.getId()))
                .andExpect(jsonPath("$.quantity").value(1))
                .andExpect(jsonPath("$.totalPrice").value(3000))
                .andExpect(jsonPath("$.remainingPoint").value(7000));

        User resultUser = userRepository.findById(user.getId()).orElseThrow();

        assertThat(resultUser.getPoint()).isEqualTo(7_000L);
        assertThat(orderRepository.count()).isEqualTo(1);

        verify(orderEventProducer, times(1))
                .send(any(OrderCompletedEvent.class));

        verify(rankingRedisRepository, times(1))
                .increaseProductScore(product.getId(), 1L);

        verify(redisLockService, times(1))
                .unlock(anyString(), anyString());
    }
}
