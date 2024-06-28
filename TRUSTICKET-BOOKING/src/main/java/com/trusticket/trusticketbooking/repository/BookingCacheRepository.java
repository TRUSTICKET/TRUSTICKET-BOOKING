package com.trusticket.trusticketbooking.repository;

import com.trusticket.trusticketbooking.dto.BookingStatus;
import lombok.AllArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.stereotype.Repository;

import java.util.concurrent.TimeUnit;


@Repository
@AllArgsConstructor
public class BookingCacheRepository {
    private final RedisTemplate<String, String> redisTemplate;

    public void insertBookingCache (Long offset, String result) {
        String key = "booking/" + offset;
        redisTemplate.opsForValue().set(key, result, 5, TimeUnit.MINUTES);
    }


    public String getBookingCache(Long offset) {
        String key = "booking/" + offset;
        return redisTemplate.opsForValue().get(key);
    }

}
