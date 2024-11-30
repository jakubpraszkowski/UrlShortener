package com.kubuski.urlshortener.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.Instant;

@Table(name = "urls")
@Entity
@Data
@Builder
public class Url {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, name = "original_url")
    private String originalUrl;

    @Column(unique = true, nullable = false, name = "short_url")
    private String shortUrl;

    @Column(name = "created_at", updatable = false)
    @CreatedDate
    private Instant createdAt;

    @Column(name = "updated_at")
    @LastModifiedDate
    private Instant updatedAt;

    @Column(name = "expiration_date")
    private Instant expirationDate;

    @Column(name = "access_count")
    private int accessCount;

    @Column(name = "deleted")
    private boolean deleted;
}
