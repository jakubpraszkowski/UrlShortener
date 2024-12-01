package com.kubuski.urlshortener.service;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import com.kubuski.urlshortener.dto.UrlRequest;
import com.kubuski.urlshortener.dto.UrlResponse;
import com.kubuski.urlshortener.entity.Url;
import com.kubuski.urlshortener.repository.UrlRepository;
import com.kubuski.urlshortener.exception.UrlNotFoundException;

@ExtendWith(MockitoExtension.class)
class UrlServiceTest {
    private static final int SHORT_URL_LENGTH = 8;

    private static final String SHORT_URL = "shortUrl";

    @Mock
    private UrlRepository urlRepository;

    @InjectMocks
    private UrlService urlService;

    private Url url;

    @BeforeEach
    public void setUp() {
        url = Url.builder().id(1L).originalUrl("https://example.com").shortUrl(SHORT_URL)
                .accessCount(0).deleted(false).build();
    }

    @Test
    void testCreateShortUrl() {
        UrlRequest urlRequest = new UrlRequest("https://example.com");
        when(urlRepository.save(any(Url.class))).thenReturn(url);
        
        UrlResponse response = urlService.createShortUrl(urlRequest);

        assertEquals(urlRequest.url(), response.originalUrl());
        assertNotNull(response.shortUrl());
        assertTrue(response.shortUrl().length() == SHORT_URL_LENGTH);
    }

    @Test
    void testGetOriginalUrl() {
        when(urlRepository.findByShortUrlAndDeletedFalse(anyString())).thenReturn(Optional.of(url));

        UrlResponse response = urlService.getOriginalUrl(SHORT_URL);

        assertEquals(url.getOriginalUrl(), response.originalUrl());
        assertEquals(url.getShortUrl(), response.shortUrl());
        assertEquals(url.getAccessCount(), response.accessCount());
    }

    @Test
    void testGetOriginalUrlNotFound() {
        when(urlRepository.findByShortUrlAndDeletedFalse(anyString())).thenReturn(Optional.empty());

        assertThrows(UrlNotFoundException.class, () -> urlService.getOriginalUrl(SHORT_URL));
    }

    @Test
    void testUpdateOriginalUrl() {
        UrlRequest urlRequest = new UrlRequest("https://newexample.com");
        when(urlRepository.findByShortUrlAndDeletedFalse(anyString())).thenReturn(Optional.of(url));

        UrlResponse response = urlService.updateOriginalUrl(SHORT_URL, urlRequest);

        assertEquals(urlRequest.url(), response.originalUrl());
        assertEquals(url.getShortUrl(), response.shortUrl());
    }

    @Test
    void testUpdateOriginalUrlNotFound() {
        UrlRequest urlRequest = new UrlRequest("https://newexample.com");
        when(urlRepository.findByShortUrlAndDeletedFalse(anyString())).thenReturn(Optional.empty());

        assertThrows(UrlNotFoundException.class,
                () -> urlService.updateOriginalUrl(SHORT_URL, urlRequest));
    }

    @Test
    void testDeleteUrl() {
        when(urlRepository.findByShortUrlAndDeletedFalse(anyString())).thenReturn(Optional.of(url));

        urlService.deleteUrl(SHORT_URL);

        assertEquals(true, url.isDeleted());
    }

    @Test
    void testDeleteUrlNotFound() {
        when(urlRepository.findByShortUrlAndDeletedFalse(anyString())).thenReturn(Optional.empty());

        assertThrows(UrlNotFoundException.class, () -> urlService.deleteUrl(SHORT_URL));
    }

    @Test
    void testGetUrlStats() {
        when(urlRepository.findByShortUrlAndDeletedFalse(anyString())).thenReturn(Optional.of(url));

        UrlResponse response = urlService.getUrlStats(SHORT_URL);

        assertEquals(url.getOriginalUrl(), response.originalUrl());
        assertEquals(url.getShortUrl(), response.shortUrl());
    }

    @Test
    void testGetUrlStatsNotFound() {
        when(urlRepository.findByShortUrlAndDeletedFalse(anyString())).thenReturn(Optional.empty());

        assertThrows(UrlNotFoundException.class, () -> urlService.getUrlStats(SHORT_URL));
    }
}
