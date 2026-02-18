package com.kubuski.urlshortener.service;

import com.kubuski.urlshortener.dto.UrlRequest;
import com.kubuski.urlshortener.dto.UrlResponse;
import com.kubuski.urlshortener.entity.Url;
import com.kubuski.urlshortener.exception.UrlNotFoundException;
import com.kubuski.urlshortener.mapper.EntityMapper;
import com.kubuski.urlshortener.repository.UrlRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UrlService {

    private final UrlRepository urlRepository;

    @Transactional
    public UrlResponse createShortUrl(UrlRequest urlRequest) {
        Url url = buildUrl(urlRequest);
        Url savedUrl = urlRepository.save(url);

        return EntityMapper.toUrlResponse(savedUrl);
    }

    @Transactional
    public UrlResponse getOriginalUrl(String shortUrl) {
        Url url = findUrlByShortUrl(shortUrl);
        incrementAccessCount(url);

        return EntityMapper.toUrlResponse(url);
    }

    @Transactional
    public UrlResponse updateOriginalUrl(String shortUrl, UrlRequest urlRequest) {
        Url url = findUrlByShortUrl(shortUrl);
        updateUrl(url, urlRequest);

        return EntityMapper.toUrlResponse(url);
    }

    @Transactional
    public void deleteUrl(String shortUrl) {
        Url url = findUrlByShortUrl(shortUrl);
        markUrlAsDeleted(url);
    }

    @Transactional(readOnly = true)
    public UrlResponse getUrlStats(String shortUrl) {
        Url url = findUrlByShortUrl(shortUrl);

        return EntityMapper.toUrlResponse(url);
    }

    private Url buildUrl(UrlRequest urlRequest) {
        return Url.builder().originalUrl(urlRequest.url()).shortUrl(generateShortCode())
                .expirationDate(null).accessCount(0).deleted(false).build();
    }

    private Url findUrlByShortUrl(String shortUrl) {
        return urlRepository.findByShortUrlAndDeletedFalse(shortUrl)
                .orElseThrow(() -> new UrlNotFoundException("URL not found: " + shortUrl));
    }

    private void incrementAccessCount(Url url) {
        url.setAccessCount(url.getAccessCount() + 1);
    }

    private void updateUrl(Url url, UrlRequest urlRequest) {
        url.setAccessCount(0);
        url.setOriginalUrl(urlRequest.url());
    }

    private void markUrlAsDeleted(Url url) {
        url.setDeleted(true);
    }

    private String generateShortCode() {
        return UUID.randomUUID().toString().substring(0, 8);
    }
}
