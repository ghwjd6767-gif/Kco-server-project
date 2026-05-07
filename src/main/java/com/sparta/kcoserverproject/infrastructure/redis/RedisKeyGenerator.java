package com.sparta.kcoserverproject.infrastructure.redis;

import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class RedisKeyGenerator {

    public String popularProductDailyKey(LocalDate date) {
        return "popular:products:daily:" + date;
    }
}
