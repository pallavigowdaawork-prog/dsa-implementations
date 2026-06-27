package com.example.cache.model;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

public class GetResponse {
    private String key;
    private String value;
    private String expiresAt;

    public GetResponse(String key, String value, Long expiresAtMillis) {
        this.key = key;
        this.value = value;
        if (expiresAtMillis != null) {
            this.expiresAt = Instant.ofEpochMilli(expiresAtMillis)
                    .atZone(ZoneId.of("UTC"))
                    .format(DateTimeFormatter.ISO_INSTANT);
        }
    }

    public String getKey() { return key; }
    public String getValue() { return value; }
    public String getExpiresAt() { return expiresAt; }
}
