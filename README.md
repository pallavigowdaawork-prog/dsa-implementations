# 🗂️ LRU Cache Service — Phase 1

> A production-style, in-memory HTTP caching service built from scratch in **Java + Spring Boot + Gradle**.
> No external caching libraries. No shortcuts. Pure data structures.

---

## 🎯 What is this project?

This is an **HTTP-based Least Recently Used (LRU) Cache Service** — a REST API that lets you store, retrieve, and expire key-value pairs in memory, powered by a custom-built LRU eviction algorithm written entirely from scratch.

This project is **Phase 1** of a hands-on DSA (Data Structures & Algorithms) implementation journey.

---

## 🏗️ Architecture

```
Client (HTTP)
     │
     ▼
CacheController          ← REST layer: handles HTTP requests/responses
     │
     ▼
CacheService             ← Business logic: orchestrates cache operations
     │
     ▼
LRUCache                 ← Core DSA: HashMap + Doubly Linked List
     │
     ├── HashMap<String, CacheNode>   → O(1) key lookups
     └── Doubly Linked List           → O(1) order tracking (MRU ↔ LRU)
```

---

## 📁 Project Structure

```
src/main/java/com/example/cache/
├── LruCacheServiceApplication.java   ← Spring Boot entry point
├── config/
│   └── CacheConfig.java              ← Wires LRUCache bean with capacity
├── controller/
│   └── CacheController.java          ← REST endpoints (PUT, GET, DELETE, stats)
├── service/
│   └── CacheService.java             ← Service layer
├── core/
│   ├── LRUCache.java                 ← THE core data structure ⭐
│   └── CacheNode.java               ← Doubly linked list node
└── model/
    ├── PutRequest.java               ← Request body for PUT
    ├── GetResponse.java              ← Response body for GET
    └── StatsResponse.java            ← Response body for /stats
```

---

## 🌐 REST API Reference

| Method   | Endpoint           | Description                             | Status Codes     |
|----------|--------------------|-----------------------------------------|------------------|
| `PUT`    | `/cache/{key}`     | Store a key-value pair (with optional TTL) | 201 Created   |
| `GET`    | `/cache/{key}`     | Retrieve a value by key                 | 200 OK / 404    |
| `DELETE` | `/cache/{key}`     | Manually evict a key                    | 204 / 404       |
| `GET`    | `/cache/stats`     | Get cache performance metrics           | 200 OK          |
| `DELETE` | `/cache/clear`     | Clear all cache entries + reset stats   | 204 No Content  |

### Example — PUT a value with TTL
```json
PUT /cache/user1
{
  "value": "Pallavi",
  "ttlSeconds": 60
}
```

### Example — GET response
```json
{
  "key": "user1",
  "value": "Pallavi",
  "expiresAt": "2026-06-27T18:30:00Z"
}
```

### Example — Stats response
```json
{
  "capacity": 100,
  "currentSize": 3,
  "hits": 12,
  "misses": 4,
  "hitRate": "75.0%",
  "evictions": 1,
  "expiredEvictions": 0
}
```

---

## 🧠 What We Learnt in Phase 1

### 1. LRU Cache — The Data Structure
- **Why LRU?** When memory is limited, the cache needs a smart eviction policy. LRU evicts the key that was accessed the **least recently** — the intuition being "if you haven't needed it in a while, you probably won't soon."
- **The naive approach** would be a list — but O(n) lookups are too slow.
- **The optimal approach:** combine a `HashMap` (O(1) lookup) with a **Doubly Linked List** (O(1) insertion and removal) to get O(1) for ALL operations.

### 2. Doubly Linked List Internals
- Every cached item is a `CacheNode` with `key`, `value`, `expiresAt`, and pointers to `prev` and `next` nodes.
- **Dummy sentinel head & tail** nodes are used to avoid null-pointer edge cases on insert/delete.
- On every `GET`, the accessed node is **moved to the head** (most recently used).
- On every `PUT` that causes overflow, the **tail node is popped** (least recently used → evicted).

### 3. Thread Safety with `synchronized`
- A cache shared across multiple HTTP requests **must be thread-safe**.
- Used `synchronized` on all mutating methods (`get`, `put`, `delete`, `clear`) to prevent race conditions on the linked list and map.

### 4. Lazy TTL Expiration
- Instead of a background timer thread, TTL is checked **lazily on every GET**.
- If a key's `expiresAt` timestamp is in the past → the node is silently removed and a `404` is returned.
- This is simpler, avoids concurrency issues with timer threads, and is a common real-world pattern (used by Redis too!).

### 5. Spring Boot REST API Design
- Learned how to wire a **plain Java class** (`LRUCache`) as a Spring `@Bean` via a `@Configuration` class.
- Used `@RestController`, `@PathVariable`, `@RequestBody` annotations.
- Used `ResponseEntity<T>` for full control over HTTP status codes (201, 204, 404 etc.).
- Read configuration from `application.properties` using `@Value`.

### 6. Metrics with `AtomicLong`
- Tracked `hits`, `misses`, `evictions`, and `expiredEvictions` using `AtomicLong`.
- `AtomicLong` ensures safe counter increments under concurrent access **without full synchronization overhead**.

### 7. Build & Run with Gradle
- Understood the role of `build.gradle`, `gradlew`, and `gradle.properties`.
- Fixed corporate SSL certificate chain issues by configuring `WINDOWS-ROOT` truststore.
- Used `./gradlew bootRun` to compile and launch the embedded Tomcat server.

### 8. Git & GitHub
- Initialized a local git repository with `git init`.
- Created a proper first commit with a meaningful commit message.
- Pushed to a remote GitHub repository using a Personal Access Token (PAT).

---

## ✅ Phase 1 — Accomplishments

| # | Accomplishment                                          | Status |
|---|---------------------------------------------------------|--------|
| 1 | Designed LRU Cache from scratch (no libraries)          | ✅ Done |
| 2 | Implemented `CacheNode` — DLL node with TTL support     | ✅ Done |
| 3 | Implemented `LRUCache` — HashMap + DLL, O(1) ops        | ✅ Done |
| 4 | Thread-safe cache with `synchronized` methods           | ✅ Done |
| 5 | Lazy TTL expiration on GET                              | ✅ Done |
| 6 | Stats tracking with `AtomicLong`                        | ✅ Done |
| 7 | Spring Boot REST API with 5 endpoints                   | ✅ Done |
| 8 | Gradle build with embedded Tomcat on port 8080          | ✅ Done |
| 9 | Pushed to GitHub                                        | ✅ Done |

---

## 🚀 How to Run

### Prerequisites
- Java 21
- Gradle (via included wrapper)

### Run locally
```bash
./gradlew bootRun
```

Server starts at: **`http://localhost:8080`**

### Quick test with PowerShell
```powershell
# Store a value
Invoke-RestMethod -Uri "http://localhost:8080/cache/hello" -Method PUT `
  -ContentType "application/json" -Body '{"value": "world", "ttlSeconds": 60}'

# Retrieve it
Invoke-RestMethod -Uri "http://localhost:8080/cache/hello" -Method GET

# Check stats
Invoke-RestMethod -Uri "http://localhost:8080/cache/stats" -Method GET
```

---

## 🔮 Phase 2 — What's Next?

| Feature                          | Description                                               |
|----------------------------------|-----------------------------------------------------------|
| 🔒 `ReentrantReadWriteLock`      | Allow concurrent reads; lock only on writes               |
| ⏰ Background TTL cleanup        | Proactive expired key removal via `ScheduledExecutorService` |
| 📊 Prometheus + Grafana metrics  | Expose `/actuator/prometheus` for real-time dashboards    |
| 🧪 Full integration tests        | `@SpringBootTest` tests hitting real HTTP endpoints       |
| 🐳 Docker & Docker Compose       | Containerize the service for deployment                   |
| 📝 Swagger / OpenAPI docs        | Auto-generated API documentation                          |

---

## 👩‍💻 Author

**Pallavi M** — First Spring Boot project! Built with 💪 and a lot of patience with firewalls.
> *"The only way to learn a new programming language is by writing programs in it."* — Dennis Ritchie
