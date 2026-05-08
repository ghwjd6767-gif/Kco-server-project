package com.sparta.kcoserverproject.domain.ranking.service;

import com.sparta.kcoserverproject.domain.product.entity.Product;
import com.sparta.kcoserverproject.domain.product.repository.ProductRepository;
import com.sparta.kcoserverproject.domain.ranking.dto.PopularProductResponse;
import com.sparta.kcoserverproject.infrastructure.redis.repository.RankingRedisRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@SpringBootTest
@Transactional
public class RankingServiceTest {

    @Autowired
    RankingService rankingService;

    @Autowired
    ProductRepository productRepository;

    @MockitoBean
    RankingRedisRepository rankingRedisRepository;

    @Test
    void 최근_7일간_인기메뉴_TOP3를_조회한다() {

        // given
        Product americano = productRepository.save(
                Product.builder()
                        .name("아메리카노")
                        .price(BigDecimal.valueOf(3000))
                        .build()
        );

        Product latte = productRepository.save(
                Product.builder()
                        .name("라떼")
                        .price(BigDecimal.valueOf(4000))
                        .build()
        );

        Product mocha = productRepository.save(
                Product.builder()
                        .name("모카")
                        .price(BigDecimal.valueOf(4500))
                        .build()
        );

        when(rankingRedisRepository.getTop3ProductIdsForLast7Days())
                .thenReturn(List.of(
                        americano.getId(),
                        latte.getId(),
                        mocha.getId()
                ));

        // when
        List<PopularProductResponse> result = rankingService.getPopularProducts();

        // then
        assertThat(result).hasSize(3);
        assertThat(result.get(0).name()).isEqualTo("아메리카노");
        assertThat(result.get(1).name()).isEqualTo("라떼");
        assertThat(result.get(2).name()).isEqualTo("모카");

        verify(rankingRedisRepository, times(1))
                .getTop3ProductIdsForLast7Days();
    }
}
