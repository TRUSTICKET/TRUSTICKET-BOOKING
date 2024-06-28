package com.trusticket.trusticketbooking.repository;

import lombok.AllArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.util.concurrent.TimeUnit;


@Repository
@AllArgsConstructor
public class EventCacheRepository {
    private final RedisTemplate<String, Integer> redisTemplate;

    public void insertEventCache (String eventId, Integer stock) {
        String key = "event/" + eventId;
        redisTemplate.opsForValue().set(key, stock, 1, TimeUnit.HOURS);
    }


    public Integer getEventCache(String eventId) {
        String key = "booking/" + eventId;
        return redisTemplate.opsForValue().get(key);
    }

}
