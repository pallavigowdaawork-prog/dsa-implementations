package com.example.cache.config;

import com.example.cache.core.LRUCache;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CacheConfig {

    @Value("${cache.capacity:100}")
    private int cacheCapacity;

    @Bean
    public LRUCache lruCache() {
        return new LRUCache(cacheCapacity);
    }
}
