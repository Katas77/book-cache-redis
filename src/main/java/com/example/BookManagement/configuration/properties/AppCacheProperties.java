package com.example.BookManagement.configuration.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.time.Duration;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@Data
@ConfigurationProperties(prefix = "app.cache")
public class AppCacheProperties {

    private final ArrayList<String> cacheNames = new ArrayList<>();
    private final Map<String, CacheProperties> caches = new HashMap<>();
    private final CacheType cacheType;

    @Data
    public static class CacheProperties {
        private Duration expiry = Duration.ZERO;

    }

    public interface CacheNames {
        String ENTITY_BY_CATEGORY = "databaseEntitiesByCategory";
        String DATABASE_ENTITY = "databaseEntity";
    }

    public enum CacheType {
        IN_MEMORY,
        REDIS
    }

}
