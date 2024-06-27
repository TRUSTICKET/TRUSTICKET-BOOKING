package com.trusticket.trusticketbooking.repository;

import lombok.AllArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;


@Repository
@AllArgsConstructor
public class BookingCancelRepository {

    private final RedisTemplate<String, String> redisTemplate;

    public void insertBookingCancel(Long offset) {
        String key = "booking-cancel/" + offset;
        redisTemplate.opsForValue().set(key, "cancelled");
    }

    public void deleteBookingCancel(Long offset) {
        String key = "booking-cancel/" + offset;
        redisTemplate.delete(key);
    }

    public String getBookingCancel(Long offset) {
        String key = "booking-cancel/" + offset;
        return redisTemplate.opsForValue().get(key);
    }

}
