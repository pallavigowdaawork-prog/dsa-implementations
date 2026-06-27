package com.example.cache.model;

public class StatsResponse {
    private int capacity;
    private int currentSize;
    private long hits;
    private long misses;
    private String hitRate;
    private long evictions;
    private long expiredEvictions;

    public StatsResponse(int capacity, int currentSize, long hits, long misses, long evictions, long expiredEvictions) {
        this.capacity = capacity;
        this.currentSize = currentSize;
        this.hits = hits;
        this.misses = misses;
        this.evictions = evictions;
        this.expiredEvictions = expiredEvictions;

        long total = hits + misses;
        this.hitRate = total == 0 ? "0.0%" : String.format("%.1f%%", ((double) hits / total) * 100);
    }

    public int getCapacity() { return capacity; }
    public int getCurrentSize() { return currentSize; }
    public long getHits() { return hits; }
    public long getMisses() { return misses; }
    public String getHitRate() { return hitRate; }
    public long getEvictions() { return evictions; }
    public long getExpiredEvictions() { return expiredEvictions; }
}
