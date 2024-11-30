package com.kubuski.urlshortener.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import com.kubuski.urlshortener.dto.UrlRequest;
import com.kubuski.urlshortener.dto.UrlResponse;
import com.kubuski.urlshortener.entity.Url;
import com.kubuski.urlshortener.repository.UrlRepository;
import com.kubuski.urlshortener.exception.UrlNotFoundException;

public class UrlServiceTest {

    private final int NUMBER_OF_OBJECTS = 2;

    @Mock
    @Autowired
    private UrlRepository urlRepository;

    @InjectMocks
    private UrlService urlService;

    private Url url;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        url = Url.builder().id(1L).originalUrl("https://example.com").shortUrl("shortUrl")
                .accessCount(0).deleted(false).build();

        urlRepository.deleteAll();
        urlRepository.save(url);
    }

    @Test
    public void testCreateShortUrl() {
        // given
        UrlRequest urlRequest = new UrlRequest("https://example.com");
        when(urlRepository.save(any(Url.class))).thenReturn(url);

        // when
        UrlResponse response = urlService.createShortUrl(urlRequest);

        // then
        assertEquals(urlRequest.url(), response.originalUrl());
        assertEquals(url.getShortUrl(), response.shortUrl());
    }

    @Test
    public void testGetOriginalUrl() {
        // Given
        String shortUrl = "shortUrl";

        when(urlRepository.findByShortUrlAndDeletedFalse(shortUrl)).thenReturn(Optional.of(url));

        // When
        UrlResponse response = urlService.getOriginalUrl(shortUrl);

        // Then
        assertEquals(url.getOriginalUrl(), response.originalUrl());
        assertEquals(url.getShortUrl(), response.shortUrl());
        assertEquals(url.getAccessCount() + 1, response.accessCount());
    }

    @Test
    public void testGetOriginalUrlNotFound() {
        // Given
        String shortUrl = "shortUrl";
        when(urlRepository.findByShortUrlAndDeletedFalse(shortUrl)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(UrlNotFoundException.class, () -> urlService.getOriginalUrl(shortUrl));
    }

    @Test
    public void testUpdateOriginalUrl() {
        // Given
        String shortUrl = "shortUrl";
        UrlRequest urlRequest = new UrlRequest("https://newexample.com");
        when(urlRepository.findByShortUrlAndDeletedFalse(shortUrl)).thenReturn(Optional.of(url));
        when(urlRepository.save(any(Url.class))).thenReturn(url);

        // When
        UrlResponse response = urlService.updateOriginalUrl(shortUrl, urlRequest);

        // Then
        assertEquals(urlRequest.url(), response.originalUrl());
        assertEquals(url.getShortUrl(), response.shortUrl());
    }

    @Test
    public void testUpdateOriginalUrlNotFound() {
        // given
        String shortUrl = "shortUrl";
        UrlRequest urlRequest = new UrlRequest("https://newexample.com");
        when(urlRepository.findByShortUrlAndDeletedFalse(shortUrl)).thenReturn(Optional.empty());

        // when & then
        assertThrows(UrlNotFoundException.class,
                () -> urlService.updateOriginalUrl(shortUrl, urlRequest));
    }

    @Test
    public void testDeleteUrl() {
        // Given
        String shortUrl = "shortUrl";
        when(urlRepository.findByShortUrlAndDeletedFalse(shortUrl)).thenReturn(Optional.of(url));

        // When
        urlService.deleteUrl(shortUrl);

        // Then
        verify(urlRepository, times(NUMBER_OF_OBJECTS)).save(url);
        assertEquals(true, url.isDeleted());
    }

    @Test
    public void testDeleteUrlNotFound() {
        // given
        String shortUrl = "shortUrl";
        when(urlRepository.findByShortUrlAndDeletedFalse(shortUrl)).thenReturn(Optional.empty());

        // when & then
        assertThrows(UrlNotFoundException.class, () -> urlService.deleteUrl(shortUrl));
    }

    @Test
    public void testGetUrlStats() {
        // Given
        String shortUrl = "shortUrl";
        when(urlRepository.findByShortUrlAndDeletedFalse(shortUrl)).thenReturn(Optional.of(url));

        // When
        UrlResponse response = urlService.getUrlStats(shortUrl);

        // Then
        assertEquals(url.getOriginalUrl(), response.originalUrl());
        assertEquals(url.getShortUrl(), response.shortUrl());
    }

    @Test
    public void testGetUrlStatsNotFound() {
        // given
        String shortUrl = "shortUrl";
        when(urlRepository.findByShortUrlAndDeletedFalse(shortUrl)).thenReturn(Optional.empty());

        // when & then
        assertThrows(UrlNotFoundException.class, () -> urlService.getUrlStats(shortUrl));
    }
}
