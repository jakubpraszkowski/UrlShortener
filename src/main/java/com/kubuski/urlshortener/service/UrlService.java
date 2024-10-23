package com.kubuski.urlshortener.service;

import org.springframework.stereotype.Service;

import com.kubuski.urlshortener.dto.UrlResponse;
import com.kubuski.urlshortener.entity.Url;
import com.kubuski.urlshortener.repository.UrlRepository;
import java.time.LocalDateTime;
import lombok.extern.log4j.Log4j2;

@Service
@Log4j2
public class UrlService{
    private UrlRepository urlRepository;

    public UrlResponse createShortUrl(String originalUrl) {
        String shortCode = generateShortCode();

        Url url = new Url();
        url.setOriginalUrl(originalUrl);
        url.setShortUrl(shortCode);
        url.setCreatedAt(LocalDateTime.now());
        url.setUpdatedAt(LocalDateTime.now());
        url.setAccessCount(0);

        Url savedUrl = urlRepository.save(url);

        return new UrlResponse(
            savedUrl.getId(),
            savedUrl.getOriginalUrl(),
            savedUrl.getShortUrl(),
            savedUrl.getCreatedAt(),
            savedUrl.getUpdatedAt()
        );
    }

    private String generateShortCode() {
        return java.util.UUID.randomUUID().toString().substring(0, 6);
    }

}
