package com.kubuski.urlshortener.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Table(name = "urls")
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Url {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, name = "original_url")
    private String originalUrl;

    @Column(unique = true, nullable = false, name = "short_url")
    private String shortUrl;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    @Column(name = "expiration_date")
    private LocalDateTime expirationDate;

    @Column(name = "access_count")
    private int accessCount;
}
