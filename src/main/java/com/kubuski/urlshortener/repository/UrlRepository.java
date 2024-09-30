package com.kubuski.urlshortener.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

import com.kubuski.urlshortener.dto.UrlDto;
import com.kubuski.urlshortener.entity.Url;

public interface UrlRepository extends JpaRepository<Url, Long> {
    Optional<Url> getUrlById(Long id);
    Optional<Url> findByShortUrl(UrlDto shortUrl);
}
