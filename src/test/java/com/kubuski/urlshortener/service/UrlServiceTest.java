package com.kubuski.urlshortener.service;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
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
    private static final String ORIGINAL_URL = "https://example.com";
    private static final String NEW_ORIGINAL_URL = "https://newexample.com";

    @Mock
    private UrlRepository urlRepository;

    @InjectMocks
    private UrlService urlService;

    private Url url;

    @BeforeEach
    public void setUp() {
        url = Url.builder().id(1L).originalUrl(ORIGINAL_URL).shortUrl(SHORT_URL).accessCount(0)
                .deleted(false).build();
    }

    @Test
    void testCreateShortUrl() {
        UrlRequest urlRequest = new UrlRequest(ORIGINAL_URL);
        when(urlRepository.save(any(Url.class))).thenReturn(url);

        UrlResponse response = urlService.createShortUrl(urlRequest);

        assertAll("Create Short URL", () -> assertEquals(urlRequest.url(), response.originalUrl()),
                () -> assertNotNull(response.shortUrl()),
                () -> assertEquals(SHORT_URL_LENGTH, response.shortUrl().length()));
    }

    @Test
    void testGetOriginalUrl() {
        when(urlRepository.findByShortUrlAndDeletedFalse(anyString())).thenReturn(Optional.of(url));

        UrlResponse response = urlService.getOriginalUrl(SHORT_URL);

        assertAll("Get Original URL",
                () -> assertEquals(url.getOriginalUrl(), response.originalUrl()),
                () -> assertEquals(url.getShortUrl(), response.shortUrl()),
                () -> assertEquals(url.getAccessCount(), response.accessCount()));
    }

    @Test
    void testGetOriginalUrlNotFound() {
        when(urlRepository.findByShortUrlAndDeletedFalse(anyString())).thenReturn(Optional.empty());

        assertThrows(UrlNotFoundException.class, () -> urlService.getOriginalUrl(SHORT_URL));
    }

    @Test
    void testUpdateOriginalUrl() {
        UrlRequest urlRequest = new UrlRequest(NEW_ORIGINAL_URL);
        when(urlRepository.findByShortUrlAndDeletedFalse(anyString())).thenReturn(Optional.of(url));

        UrlResponse response = urlService.updateOriginalUrl(SHORT_URL, urlRequest);

        assertAll("Update Original URL",
                () -> assertEquals(urlRequest.url(), response.originalUrl()),
                () -> assertEquals(url.getShortUrl(), response.shortUrl()));
    }

    @Test
    void testUpdateOriginalUrlNotFound() {
        UrlRequest urlRequest = new UrlRequest(NEW_ORIGINAL_URL);
        when(urlRepository.findByShortUrlAndDeletedFalse(anyString())).thenReturn(Optional.empty());

        assertThrows(UrlNotFoundException.class,
                () -> urlService.updateOriginalUrl(SHORT_URL, urlRequest));
    }

    @Test
    void testDeleteUrl() {
        when(urlRepository.findByShortUrlAndDeletedFalse(anyString())).thenReturn(Optional.of(url));

        urlService.deleteUrl(SHORT_URL);

        assertTrue(url.isDeleted());
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

        assertAll("Get URL Stats", () -> assertEquals(url.getOriginalUrl(), response.originalUrl()),
                () -> assertEquals(url.getShortUrl(), response.shortUrl()));
    }

    @Test
    void testGetUrlStatsNotFound() {
        when(urlRepository.findByShortUrlAndDeletedFalse(anyString())).thenReturn(Optional.empty());

        assertThrows(UrlNotFoundException.class, () -> urlService.getUrlStats(SHORT_URL));
    }
}
