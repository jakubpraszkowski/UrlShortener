package com.kubuski.urlshortener.service;

import org.springframework.stereotype.Service;

import com.kubuski.urlshortener.dto.UrlRequest;
import com.kubuski.urlshortener.dto.UrlResponse;
import com.kubuski.urlshortener.entity.Url;
import com.kubuski.urlshortener.exceptions.ResourceNotFoundException;
import com.kubuski.urlshortener.repository.UrlRepository;
import java.time.Instant;

@Service
public class UrlService{
    private final UrlRepository urlRepository;

    public UrlService(UrlRepository urlRepository) {
        this.urlRepository = urlRepository;
    }

    public UrlResponse createShortUrl(UrlRequest originalUrl) {
        Url url = new Url();
        url.setOriginalUrl(originalUrl.toString());
        url.setShortUrl(generateShortCode());
        url.setCreatedAt(Instant.now());
        url.setUpdatedAt(Instant.now());
        url.setExpirationDate(null);
        url.setAccessCount(0);

        Url savedUrl = urlRepository.save(url);

        return new UrlResponse(
            savedUrl.getId(),
            savedUrl.getOriginalUrl(),
            savedUrl.getShortUrl(),
            savedUrl.getCreatedAt(),
            savedUrl.getUpdatedAt(),
            savedUrl.getExpirationDate(),
            savedUrl.getAccessCount()
        );
    }

    public UrlResponse getOriginalUrl(String shortUrl) {
        Url url = urlRepository.findByShortUrl(shortUrl).orElseThrow(() -> new ResourceNotFoundException("URL not found: " + shortUrl));

        url.setAccessCount(url.getAccessCount() + 1);

        Url updatedUrl = urlRepository.save(url);

        return new UrlResponse(
            updatedUrl.getId(),
            updatedUrl.getOriginalUrl(),
            updatedUrl.getShortUrl(),
            updatedUrl.getCreatedAt(),
            updatedUrl.getUpdatedAt(),
            updatedUrl.getExpirationDate(),
            updatedUrl.getAccessCount()
        );
    }

    public UrlResponse updateOriginalUrl(String shortUrl, UrlRequest urlRequest) {
        Url url = urlRepository.findByShortUrl(shortUrl).orElseThrow(() -> new ResourceNotFoundException("URL not found: " + shortUrl));

        url.setAccessCount(0);
        url.setUpdatedAt(Instant.now());
        url.setOriginalUrl(urlRequest.getOriginalUrl());

        Url updatedUrl = urlRepository.save(url);

        return new UrlResponse(
            updatedUrl.getId(),
            updatedUrl.getOriginalUrl(),
            updatedUrl.getShortUrl(),
            updatedUrl.getCreatedAt(),
            updatedUrl.getUpdatedAt(),
            updatedUrl.getExpirationDate(),
            updatedUrl.getAccessCount()
        );
    }

    public UrlResponse deleteUrl(String shortUrl) {

    }

    public UrlResponse getUrlStats(String shortUrl) {

    }

    private String generateShortCode() {
        return java.util.UUID.randomUUID().toString().substring(0, 6);
    }

}
