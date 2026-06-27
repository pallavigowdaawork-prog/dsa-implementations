package com.example.cache.core;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

public class LRUCache {
    private final int capacity;
    private final Map<String, CacheNode> cache;
    private CacheNode head;
    private CacheNode tail;

    private final AtomicLong hits = new AtomicLong(0);
    private final AtomicLong misses = new AtomicLong(0);
    private final AtomicLong evictions = new AtomicLong(0);
    private final AtomicLong expiredEvictions = new AtomicLong(0);

    public LRUCache(int capacity) {
        this.capacity = capacity;
        this.cache = new HashMap<>();

        // Dummy head and tail sentinels
        this.head = new CacheNode("", "", null);
        this.tail = new CacheNode("", "", null);
        this.head.next = this.tail;
        this.tail.prev = this.head;
    }

    public synchronized CacheNode get(String key) {
        if (!cache.containsKey(key)) {
            misses.incrementAndGet();
            return null;
        }

        CacheNode node = cache.get(key);

        // Lazy TTL expiry check
        if (node.expiresAt != null && node.expiresAt < System.currentTimeMillis()) {
            removeNode(node);
            cache.remove(key);
            expiredEvictions.incrementAndGet();
            misses.incrementAndGet();
            return null;
        }

        hits.incrementAndGet();
        moveToHead(node);
        return node;
    }

    public synchronized void put(String key, String value, Long ttlSeconds) {
        Long expiresAt = ttlSeconds != null ? System.currentTimeMillis() + (ttlSeconds * 1000) : null;

        if (cache.containsKey(key)) {
            CacheNode node = cache.get(key);
            node.value = value;
            node.expiresAt = expiresAt;
            moveToHead(node);
        } else {
            CacheNode newNode = new CacheNode(key, value, expiresAt);
            cache.put(key, newNode);
            addNode(newNode);

            if (cache.size() > capacity) {
                CacheNode lru = popTail();
                cache.remove(lru.key);
                evictions.incrementAndGet();
            }
        }
    }

    public synchronized boolean delete(String key) {
        if (cache.containsKey(key)) {
            CacheNode node = cache.get(key);
            removeNode(node);
            cache.remove(key);
            return true;
        }
        return false;
    }

    public synchronized void clear() {
        cache.clear();
        this.head.next = this.tail;
        this.tail.prev = this.head;
        hits.set(0);
        misses.set(0);
        evictions.set(0);
        expiredEvictions.set(0);
    }

    private void addNode(CacheNode node) {
        node.prev = head;
        node.next = head.next;
        head.next.prev = node;
        head.next = node;
    }

    private void removeNode(CacheNode node) {
        node.prev.next = node.next;
        node.next.prev = node.prev;
    }

    private void moveToHead(CacheNode node) {
        removeNode(node);
        addNode(node);
    }

    private CacheNode popTail() {
        CacheNode res = tail.prev;
        removeNode(res);
        return res;
    }

    public int getCapacity() { return capacity; }
    public synchronized int getCurrentSize() { return cache.size(); }
    public long getHits() { return hits.get(); }
    public long getMisses() { return misses.get(); }
    public long getEvictions() { return evictions.get(); }
    public long getExpiredEvictions() { return expiredEvictions.get(); }
}
