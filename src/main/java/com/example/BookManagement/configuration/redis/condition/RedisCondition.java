package com.example.BookManagement.configuration.redis.condition;

import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.type.AnnotatedTypeMetadata;

public class RedisCondition implements Condition {
    @Override
    public boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata) {
        // Подключаем Redis только если это включено в константах и тип кеша REDIS
        return AppConstants.REDIS_ENABLED && AppConstants.CACHE_TYPE == AppConstants.CacheType.REDIS;
    }
}
