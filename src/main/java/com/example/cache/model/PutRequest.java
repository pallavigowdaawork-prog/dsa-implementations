package com.example.cache.model;

public class PutRequest {
    private String value;
    private Long ttlSeconds;

    public String getValue() { return value; }
    public void setValue(String value) { this.value = value; }
    public Long getTtlSeconds() { return ttlSeconds; }
    public void setTtlSeconds(Long ttlSeconds) { this.ttlSeconds = ttlSeconds; }
}
