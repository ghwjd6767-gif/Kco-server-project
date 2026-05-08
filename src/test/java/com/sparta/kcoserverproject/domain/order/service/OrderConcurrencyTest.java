package com.sparta.kcoserverproject.domain.order.service;

import com.sparta.kcoserverproject.domain.order.dto.OrderRequestDto;
import com.sparta.kcoserverproject.domain.order.repository.OrderRepository;
import com.sparta.kcoserverproject.domain.product.entity.Product;
import com.sparta.kcoserverproject.domain.product.repository.ProductRepository;
import com.sparta.kcoserverproject.domain.user.entity.User;
import com.sparta.kcoserverproject.domain.user.repository.UserRepository;
import com.sparta.kcoserverproject.infrastructure.kafka.producer.OrderEventProducer;
import com.sparta.kcoserverproject.infrastructure.redis.repository.RankingRedisRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.math.BigDecimal;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class OrderConcurrencyTest {

    @Autowired
    OrderService orderService;

    @Autowired
    UserRepository userRepository;

    @Autowired
    ProductRepository productRepository;

    @Autowired
    OrderRepository orderRepository;

    @MockitoBean
    OrderEventProducer orderEventProducer;

    @MockitoBean
    RankingRedisRepository rankingRedisRepository;

    @Test
    void 동시에_여러_주문이_들어와도_포인트가_음수가_되지_않는다() throws InterruptedException {

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

        int threadCount = 10;

        ExecutorService executorService = Executors.newFixedThreadPool(threadCount);
        CountDownLatch readyLatch = new CountDownLatch(threadCount);
        CountDownLatch startLatch = new CountDownLatch(1);
        CountDownLatch doneLatch = new CountDownLatch(threadCount);

        AtomicInteger successCount = new AtomicInteger();
        AtomicInteger failCount = new AtomicInteger();

        // when
        for (int i = 0; i < threadCount; i++) {
            executorService.submit(() -> {
                try {
                    readyLatch.countDown();
                    startLatch.await();

                    OrderRequestDto request = new OrderRequestDto(
                            user.getId(),
                            product.getId(),
                            1L
                    );

                    orderService.order(request);
                    successCount.incrementAndGet();
                } catch (Exception e) {
                    failCount.incrementAndGet();
                } finally {
                    doneLatch.countDown();
                }
            });
        }

        readyLatch.await();
        startLatch.countDown();
        doneLatch.await();

        executorService.shutdown();

        // then
        User resultUser = userRepository.findById(user.getId()).orElseThrow();

        assertThat(successCount.get()).isEqualTo(3);
        assertThat(failCount.get()).isEqualTo(7);
        assertThat(resultUser.getPoint()).isEqualTo(1_000L);
        assertThat(orderRepository.count()).isEqualTo(3);
    }
}
