package com.kubuski.urlshortener.dto;

import java.time.Instant;

public record UrlResponse(
    Long id,
    String originalUrl,
    String shortUrl,
    Instant createdAt,
    Instant updatedAt,
    Instant expirationDate,
    int accessCount
    ) {}
