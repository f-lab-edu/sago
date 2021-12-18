package com.dhmall.config;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DataSourceTemplateGenerator implements DataSourceTemplate{

    private final RedisConfig redisConfig;

    @Override
    public RedisTemplate redis() {
        return redisConfig.redisTemplate(redisConfig.redisRepositoryFactory());
    }
}