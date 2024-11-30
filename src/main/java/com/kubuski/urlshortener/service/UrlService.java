package com.kubuski.urlshortener.service;

import com.kubuski.urlshortener.dto.UrlRequest;
import com.kubuski.urlshortener.dto.UrlResponse;
import com.kubuski.urlshortener.entity.Url;
import com.kubuski.urlshortener.exception.UrlIsNullException;
import com.kubuski.urlshortener.exception.UrlNotFoundException;
import com.kubuski.urlshortener.repository.UrlRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@AllArgsConstructor
public class UrlService {
    private final UrlRepository urlRepository;

    public UrlResponse createShortUrl(UrlRequest originalUrl) {

        Url url = Url.builder().originalUrl(originalUrl.url()).shortUrl(generateShortCode())
                .expirationDate(null).accessCount(0).deleted(false).build();

        Url savedUrl = urlRepository.save(url);

        return toUrlResponse(savedUrl);
    }

    public UrlResponse getOriginalUrl(String shortUrl) {
        Url url = urlRepository.findByShortUrlAndDeletedFalse(shortUrl)
                .orElseThrow(() -> new UrlNotFoundException("URL not found: " + shortUrl));

        url.setAccessCount(url.getAccessCount() + 1);

        Url updatedUrl = urlRepository.save(url);

        return toUrlResponse(updatedUrl);
    }

    public UrlResponse updateOriginalUrl(String shortUrl, UrlRequest urlRequest) {
        Url url = urlRepository.findByShortUrlAndDeletedFalse(shortUrl)
                .orElseThrow(() -> new UrlNotFoundException("URL not found: " + shortUrl));

        url.setAccessCount(0);
        url.setOriginalUrl(urlRequest.url());

        Url updatedUrl = urlRepository.save(url);

        return toUrlResponse(updatedUrl);
    }

    public void deleteUrl(String shortUrl) {
        Url url = urlRepository.findByShortUrlAndDeletedFalse(shortUrl)
                .orElseThrow(() -> new UrlNotFoundException("URL not found: " + shortUrl));

        url.setDeleted(true);
        urlRepository.save(url);
    }

    public UrlResponse getUrlStats(String shortUrl) {
        Url url = urlRepository.findByShortUrlAndDeletedFalse(shortUrl)
                .orElseThrow(() -> new UrlNotFoundException("URL not found: " + shortUrl));

        return toUrlResponse(url);
    }

    private String generateShortCode() {
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
