package com.example.BookManagement.configuration.redis;

import com.example.BookManagement.configuration.redis.condition.AppConstants;
import com.example.BookManagement.configuration.redis.condition.RedisCondition;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;

import java.util.HashMap;
import java.util.Map;

@Configuration
@EnableCaching
@Conditional(RedisCondition.class) //условие (Conditional)
public class CacheConfiguration {

    @Bean
    public CacheManager redisCacheManager(LettuceConnectionFactory lettuceConnectionFactory) {
        RedisCacheConfiguration defaultConfig = RedisCacheConfiguration.defaultCacheConfig();

        Map<String, RedisCacheConfiguration> cacheConfigurations = new HashMap<>();

        cacheConfigurations.put(
                AppConstants.CACHE_DATABASE_ENTITY,
                RedisCacheConfiguration.defaultCacheConfig().entryTtl(AppConstants.TTL_DATABASE_ENTITY)
        );

        cacheConfigurations.put(
                AppConstants.CACHE_DATABASE_ENTITIES_BY_CATEGORY,
                RedisCacheConfiguration.defaultCacheConfig().entryTtl(AppConstants.TTL_DATABASE_ENTITIES_BY_CATEGORY)
        );

        return RedisCacheManager.builder(lettuceConnectionFactory)
                .cacheDefaults(defaultConfig)
                .withInitialCacheConfigurations(cacheConfigurations)
                .build();
    }
}
