package com.kubuski.urlshortener.repository;

import com.kubuski.urlshortener.entity.Url;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

public interface UrlRepository extends JpaRepository<Url, Long> {
    Optional<Url> findById(Long id);

    Optional<Url> findByShortUrlAndDeletedFalse(String shortUrl);

    List<Url> findAllByDeletedTrueAndUpdatedAtBefore(Instant cutoffDate);
}
