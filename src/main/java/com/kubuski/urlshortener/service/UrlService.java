package com.kubuski.urlshortener.service;

import org.springframework.stereotype.Service;

import com.kubuski.urlshortener.dto.UrlRequest;
import com.kubuski.urlshortener.dto.UrlResponse;
import com.kubuski.urlshortener.entity.Url;
import com.kubuski.urlshortener.repository.UrlRepository;

@Service
public class UrlService {
    private final UrlRepository urlRepository;

    public UrlService(UrlRepository urlRepository) {
        this.urlRepository = urlRepository;
    }

    public UrlResponse createShortUrl(UrlRequest originalUrl) {
        Url url = new Url();
        url.setOriginalUrl(originalUrl.url());
        url.setShortUrl(generateShortCode());
        url.setExpirationDate(null);
        url.setAccessCount(0);
        url.setDeleted(false);

        Url savedUrl = urlRepository.save(url);

        return new UrlResponse(
                savedUrl.getId(),
                savedUrl.getOriginalUrl(),
                savedUrl.getShortUrl(),
                savedUrl.getCreatedAt(),
                savedUrl.getUpdatedAt(),
                savedUrl.getExpirationDate(),
                savedUrl.getAccessCount());
    }

    public UrlResponse getOriginalUrl(String shortUrl) {
        Url url = urlRepository.findByShortUrlAndDeletedFalse(shortUrl)
                .orElseThrow(() -> new RuntimeException("URL not found: " + shortUrl));

        url.setAccessCount(url.getAccessCount() + 1);

        Url updatedUrl = urlRepository.save(url);

        return new UrlResponse(
                updatedUrl.getId(),
                updatedUrl.getOriginalUrl(),
                updatedUrl.getShortUrl(),
                updatedUrl.getCreatedAt(),
                updatedUrl.getUpdatedAt(),
                updatedUrl.getExpirationDate(),
                updatedUrl.getAccessCount());
    }

    public UrlResponse updateOriginalUrl(String shortUrl, UrlRequest urlRequest) {
        Url url = urlRepository.findByShortUrlAndDeletedFalse(shortUrl)
                .orElseThrow(() -> new RuntimeException("URL not found: " + shortUrl));

        url.setAccessCount(0);

        Url updatedUrl = urlRepository.save(url);

        return new UrlResponse(
                updatedUrl.getId(),
                updatedUrl.getOriginalUrl(),
                updatedUrl.getShortUrl(),
                updatedUrl.getCreatedAt(),
                updatedUrl.getUpdatedAt(),
                updatedUrl.getExpirationDate(),
                updatedUrl.getAccessCount());
    }

    public void deleteUrl(String shortUrl) {
        Url url = urlRepository.findByShortUrlAndDeletedFalse(shortUrl)
                .orElseThrow(() -> new RuntimeException("URL not found: " + shortUrl));

        url.setDeleted(false);
        urlRepository.save(url);
    }

    public UrlResponse getUrlStats(String shortUrl) {
        Url url = urlRepository.findByShortUrlAndDeletedFalse(shortUrl)
                .orElseThrow(() -> new RuntimeException("URL not found: " + shortUrl));

        return new UrlResponse(
                url.getId(),
                url.getOriginalUrl(),
                url.getShortUrl(),
                url.getCreatedAt(),
                url.getUpdatedAt(),
                url.getExpirationDate(),
                url.getAccessCount());
    }

    private String generateShortCode() {
        return java.util.UUID.randomUUID().toString().substring(0, 8);
    }

}
