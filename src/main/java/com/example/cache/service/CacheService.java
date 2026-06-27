package com.example.cache.service;

import com.example.cache.core.CacheNode;
import com.example.cache.core.LRUCache;
import com.example.cache.model.GetResponse;
import com.example.cache.model.StatsResponse;
import org.springframework.stereotype.Service;

@Service
public class CacheService {

    private final LRUCache lruCache;

    public CacheService(LRUCache lruCache) {
        this.lruCache = lruCache;
    }

    public void put(String key, String value, Long ttlSeconds) {
        lruCache.put(key, value, ttlSeconds);
    }

    public GetResponse get(String key) {
        CacheNode node = lruCache.get(key);
        if (node == null) return null;
        return new GetResponse(node.key, node.value, node.expiresAt);
    }

    public boolean delete(String key) {
        return lruCache.delete(key);
    }

    public StatsResponse getStats() {
        return new StatsResponse(
                lruCache.getCapacity(),
                lruCache.getCurrentSize(),
                lruCache.getHits(),
                lruCache.getMisses(),
                lruCache.getEvictions(),
                lruCache.getExpiredEvictions()
        );
    }

    public void clear() {
        lruCache.clear();
    }
}
