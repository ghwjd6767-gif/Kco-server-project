package com.sparta.kcoserverproject.global.lock.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.time.Duration;

@Component
@RequiredArgsConstructor
public class RedisLockService {

    private final StringRedisTemplate redisTemplate;

    public boolean tryLock(String key, String value, long timeoutSeconds) {
        Boolean result = redisTemplate.opsForValue()
                .setIfAbsent(key, value, Duration.ofSeconds(timeoutSeconds));

        return Boolean.TRUE.equals(result);
    }

    public void unlock(String key, String value) {
        String savedValue = redisTemplate.opsForValue().get(key);

        if (value.equals(savedValue)) {
            redisTemplate.delete(key);
        }
    }
}
