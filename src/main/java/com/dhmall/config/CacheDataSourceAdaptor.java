package com.dhmall.config;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CacheDataSourceAdaptor implements DataSource {

    private final RedisConfig redisConfig;

    @Override
    public RedisTemplate cacheDataSource() {
        return redisConfig.redisTemplate(redisConfig.redisRepositoryFactory());
    }
}