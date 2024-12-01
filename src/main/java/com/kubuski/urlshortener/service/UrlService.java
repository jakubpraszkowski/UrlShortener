package com.kubuski.urlshortener.service;

import com.kubuski.urlshortener.dto.UrlRequest;
import com.kubuski.urlshortener.dto.UrlResponse;
import com.kubuski.urlshortener.entity.Url;
import com.kubuski.urlshortener.exception.UrlIsNullException;
import com.kubuski.urlshortener.exception.UrlNotFoundException;
import com.kubuski.urlshortener.repository.UrlRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UrlService {

    private final UrlRepository urlRepository;

    public UrlResponse createShortUrl(UrlRequest originalUrl) {

        Url url = Url.builder().originalUrl(originalUrl.url()).shortUrl(generateShortCode())
                .expirationDate(null).accessCount(0).deleted(false).build();

        return toUrlResponse(urlRepository.save(url));
    }

    @Transactional
    public UrlResponse getOriginalUrl(String shortUrl) {
        Url url = urlRepository.findByShortUrlAndDeletedFalse(shortUrl)
                .orElseThrow(() -> new UrlNotFoundException("URL not found: " + shortUrl));

        url.setAccessCount(url.getAccessCount() + 1);

        return toUrlResponse(url);
    }

    @Transactional
    public UrlResponse updateOriginalUrl(String shortUrl, UrlRequest urlRequest) {
        Url url = urlRepository.findByShortUrlAndDeletedFalse(shortUrl)
                .orElseThrow(() -> new UrlNotFoundException("URL not found: " + shortUrl));

        url.setAccessCount(0);
        url.setOriginalUrl(urlRequest.url());

        return toUrlResponse(url);
    }

    @Transactional
    public void deleteUrl(String shortUrl) {
        Url url = urlRepository.findByShortUrlAndDeletedFalse(shortUrl)
                .orElseThrow(() -> new UrlNotFoundException("URL not found: " + shortUrl));

        url.setDeleted(true);
    }

    public UrlResponse getUrlStats(String shortUrl) {
        Url url = urlRepository.findByShortUrlAndDeletedFalse(shortUrl)
                .orElseThrow(() -> new UrlNotFoundException("URL not found: " + shortUrl));

        return toUrlResponse(url);
    }

    public String generateShortCode() {
        return UUID.randomUUID().toString().substring(0, 8);
    }

    private UrlResponse toUrlResponse(Url url) {
        if (url == null) {
            throw new UrlIsNullException("Url is null");
        }

        return new UrlResponse(url.getId(), url.getOriginalUrl(), url.getShortUrl(),
                url.getCreatedAt(), url.getUpdatedAt(), url.getExpirationDate(),
                url.getAccessCount());
    }
}
