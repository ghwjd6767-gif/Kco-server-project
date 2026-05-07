package com.sparta.kcoserverproject.domain.ranking.service;

import com.sparta.kcoserverproject.domain.product.repository.ProductRepository;
import com.sparta.kcoserverproject.domain.ranking.dto.PopularProductResponse;
import com.sparta.kcoserverproject.infrastructure.redis.repository.RankingRedisRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RankingService {

    private final RankingRedisRepository rankingRedisRepository;
    private final ProductRepository productRepository;

    public List<PopularProductResponse> getPopularProducts() {
        List<Long> productIds = rankingRedisRepository.getTop3ProductIdsForLast7Days();

        return productRepository.findAllById(productIds)
                .stream()
                .map(PopularProductResponse::from)
                .toList();
    }
}
