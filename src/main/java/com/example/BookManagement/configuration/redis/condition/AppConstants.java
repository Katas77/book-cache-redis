package com.example.BookManagement.configuration.redis.condition;

import java.time.Duration;

public final class AppConstants {

    private AppConstants() {}

    // Включение Redis
    public static final boolean REDIS_ENABLED = true;

    // Redis connection
    public static final String REDIS_HOST = "localhost";
    public static final int REDIS_PORT = 6379;

    public static final CacheType CACHE_TYPE = CacheType.REDIS;

    // Имена кешей
    public static final String CACHE_DATABASE_ENTITY = "databaseEntity";
    public static final String CACHE_DATABASE_ENTITIES_BY_CATEGORY = "databaseEntitiesByCategory";

    // TTL для кешей
    public static final Duration TTL_DATABASE_ENTITY = Duration.ofMinutes(10);
    public static final Duration TTL_DATABASE_ENTITIES_BY_CATEGORY = Duration.ofMinutes(10);

    // Тип кеша
    public enum CacheType { IN_MEMORY, REDIS }
}
