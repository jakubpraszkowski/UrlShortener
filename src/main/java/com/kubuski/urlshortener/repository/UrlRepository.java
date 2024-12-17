package com.kubuski.urlshortener.repository;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

import com.kubuski.urlshortener.entity.Url;

public interface UrlRepository extends JpaRepository<Url, Long> {
    Optional<Url> getUrlById(Long id);
    // Optional<Url> findByShortUrl(String shortUrl);
    Optional<Url> findByShortUrlAndDeletedFalse(String shortUrl);
    List<Url> findAllByDeletedTrueAndUpdatedAtBefore(Instant cutoffDate);
}