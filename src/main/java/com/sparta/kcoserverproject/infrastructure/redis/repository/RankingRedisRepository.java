package com.sparta.kcoserverproject.infrastructure.redis.repository;

import com.sparta.kcoserverproject.infrastructure.redis.RedisKeyGenerator;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Repository
@RequiredArgsConstructor
public class RankingRedisRepository {

    private final StringRedisTemplate redisTemplate;
    private final RedisKeyGenerator redisKeyGenerator;

    public void increaseProductScore(Long productId, Long quantity) {
        String key = redisKeyGenerator.popularProductDailyKey(LocalDate.now());

        redisTemplate.opsForZSet()
                .incrementScore(key, String.valueOf(productId), quantity);
    }

    public List<Long> getTop3ProductIdsForLast7Days() {
        Map<String, Double> scoreMap = new HashMap<>();

        for (int i = 0; i < 7; i++) {
            String key = redisKeyGenerator.popularProductDailyKey(LocalDate.now().minusDays(i));

            Set<ZSetOperations.TypedTuple<String>> tuples =
                    redisTemplate.opsForZSet().reverseRangeWithScores(key, 0, -1);

            if (tuples == null) {
                continue;
            }

            for (ZSetOperations.TypedTuple<String> tuple : tuples) {
                String productId = tuple.getValue();
                Double score = tuple.getScore();

                if (productId == null || score == null) {
                    continue;
                }

                scoreMap.put(productId, scoreMap.getOrDefault(productId, 0.0) + score);
            }
        }

        return scoreMap.entrySet()
                .stream()
                .sorted(Map.Entry.<String, Double> comparingByValue().reversed())
                .limit(3)
                .map(entry -> Long.valueOf(entry.getKey()))
                .toList();
    }
}
