package com.example.cache.controller;

import com.example.cache.model.GetResponse;
import com.example.cache.model.PutRequest;
import com.example.cache.model.StatsResponse;
import com.example.cache.service.CacheService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/cache")
public class CacheController {

    private final CacheService cacheService;

    public CacheController(CacheService cacheService) {
        this.cacheService = cacheService;
    }

    @PutMapping("/{key}")
    public ResponseEntity<Void> put(@PathVariable String key, @RequestBody PutRequest request) {
        if (request == null || request.getValue() == null) {
            return ResponseEntity.badRequest().build();
        }
        cacheService.put(key, request.getValue(), request.getTtlSeconds());
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping("/{key}")
    public ResponseEntity<GetResponse> get(@PathVariable String key) {
        GetResponse response = cacheService.get(key);
        if (response == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{key}")
    public ResponseEntity<Void> delete(@PathVariable String key) {
        boolean deleted = cacheService.delete(key);
        return deleted ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }

    @GetMapping("/stats")
    public ResponseEntity<StatsResponse> getStats() {
        return ResponseEntity.ok(cacheService.getStats());
    }

    @DeleteMapping("/clear")
    public ResponseEntity<Void> clear() {
        cacheService.clear();
        return ResponseEntity.noContent().build();
    }
}
