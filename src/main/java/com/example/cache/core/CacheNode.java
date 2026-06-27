package com.example.cache.core;

public class CacheNode {
    public String key;
    public String value;
    public Long expiresAt; // epoch millis, null = no expiry
    public CacheNode prev;
    public CacheNode next;

    public CacheNode(String key, String value, Long expiresAt) {
        this.key = key;
        this.value = value;
        this.expiresAt = expiresAt;
    }
}
