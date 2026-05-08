package com.sparta.kcoserverproject.domain.order.service;

import com.sparta.kcoserverproject.domain.order.dto.OrderRequestDto;
import com.sparta.kcoserverproject.domain.order.repository.OrderRepository;
import com.sparta.kcoserverproject.domain.product.entity.Product;
import com.sparta.kcoserverproject.domain.product.repository.ProductRepository;
import com.sparta.kcoserverproject.domain.user.entity.User;
import com.sparta.kcoserverproject.domain.user.repository.UserRepository;
import com.sparta.kcoserverproject.global.exception.BusinessException;
import com.sparta.kcoserverproject.global.lock.service.RedisLockService;
import com.sparta.kcoserverproject.infrastructure.kafka.event.OrderCompletedEvent;
import com.sparta.kcoserverproject.infrastructure.kafka.producer.OrderEventProducer;
import com.sparta.kcoserverproject.infrastructure.redis.repository.RankingRedisRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@SpringBootTest
@Transactional
class OrderServiceTest {

    @Autowired
    OrderService orderService;

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
    void 주문_결제에_성공하면_포인트가_차감되고_주문이_생성된다() {
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

        // when
        orderService.order(request);

        // then
        User resultUser = userRepository.findById(user.getId()).orElseThrow();

        assertThat(resultUser.getPoint()).isEqualTo(7_000L);
        assertThat(orderRepository.count()).isEqualTo(1);
    }

    @Test
    void 포인트가_부족하면_주문에_실패한다() {
        // given
        User user = userRepository.save(
                User.builder()
                        .point(1_000L)
                        .build()
        );

        Product product = productRepository.save(
                Product.builder()
                        .name("라떼")
                        .price(BigDecimal.valueOf(4000))
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
        assertThatThrownBy(() -> orderService.order(request))
                .isInstanceOf(BusinessException.class);

        assertThat(orderRepository.count()).isZero();
    }

    @Test
    void 주문_성공시_Kafka_이벤트를_발행한다() {
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

        // when
        orderService.order(request);

        // then
        verify(orderEventProducer, times(1))
                .send(any(OrderCompletedEvent.class));
    }
}