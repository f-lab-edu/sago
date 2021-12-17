package com.dhmall.config;

import org.springframework.data.redis.core.RedisTemplate;

public interface DataSourceTemplate<T> {
    RedisTemplate<T, T> redis();
}
