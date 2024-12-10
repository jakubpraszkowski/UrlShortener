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
        this.url = Url.builder().id(1L).originalUrl(UrlServiceTest.ORIGINAL_URL).shortUrl(UrlServiceTest.SHORT_URL).accessCount(0)
                .deleted(false).build();
    }

    @Test
    void testCreateShortUrl() {
        final UrlRequest urlRequest = new UrlRequest(UrlServiceTest.ORIGINAL_URL);
        when(this.urlRepository.save(any(Url.class))).thenReturn(this.url);

        final UrlResponse response = this.urlService.createShortUrl(urlRequest);

        assertAll("Create Short URL", () -> assertEquals(urlRequest.url(), response.originalUrl()),
                () -> assertNotNull(response.shortUrl()),
                () -> assertEquals(UrlServiceTest.SHORT_URL_LENGTH, response.shortUrl().length()));
    }

    @Test
    void testGetOriginalUrl() {
        when(this.urlRepository.findByShortUrlAndDeletedFalse(anyString())).thenReturn(Optional.of(this.url));

        final UrlResponse response = this.urlService.getOriginalUrl(UrlServiceTest.SHORT_URL);

        assertAll("Get Original URL",
                () -> assertEquals(this.url.getOriginalUrl(), response.originalUrl()),
                () -> assertEquals(this.url.getShortUrl(), response.shortUrl()),
                () -> assertEquals(this.url.getAccessCount(), response.accessCount()));
    }

    @Test
    void testGetOriginalUrlNotFound() {
        when(this.urlRepository.findByShortUrlAndDeletedFalse(anyString())).thenReturn(Optional.empty());

        assertThrows(UrlNotFoundException.class, () -> this.urlService.getOriginalUrl(UrlServiceTest.SHORT_URL));
    }

    @Test
    void testUpdateOriginalUrl() {
        final UrlRequest urlRequest = new UrlRequest(UrlServiceTest.NEW_ORIGINAL_URL);
        when(this.urlRepository.findByShortUrlAndDeletedFalse(anyString())).thenReturn(Optional.of(this.url));

        final UrlResponse response = this.urlService.updateOriginalUrl(UrlServiceTest.SHORT_URL, urlRequest);

        assertAll("Update Original URL",
                () -> assertEquals(urlRequest.url(), response.originalUrl()),
                () -> assertEquals(this.url.getShortUrl(), response.shortUrl()));
    }

    @Test
    void testUpdateOriginalUrlNotFound() {
        final UrlRequest urlRequest = new UrlRequest(UrlServiceTest.NEW_ORIGINAL_URL);
        when(this.urlRepository.findByShortUrlAndDeletedFalse(anyString())).thenReturn(Optional.empty());

        assertThrows(UrlNotFoundException.class,
                () -> this.urlService.updateOriginalUrl(UrlServiceTest.SHORT_URL, urlRequest));
    }

    @Test
    void testDeleteUrl() {
        when(this.urlRepository.findByShortUrlAndDeletedFalse(anyString())).thenReturn(Optional.of(this.url));

        this.urlService.deleteUrl(UrlServiceTest.SHORT_URL);

        assertTrue(this.url.isDeleted());
    }

    @Test
    void testDeleteUrlNotFound() {
        when(this.urlRepository.findByShortUrlAndDeletedFalse(anyString())).thenReturn(Optional.empty());

        assertThrows(UrlNotFoundException.class, () -> this.urlService.deleteUrl(UrlServiceTest.SHORT_URL));
    }

    @Test
    void testGetUrlStats() {
        when(this.urlRepository.findByShortUrlAndDeletedFalse(anyString())).thenReturn(Optional.of(this.url));

        final UrlResponse response = this.urlService.getUrlStats(UrlServiceTest.SHORT_URL);

        assertAll("Get URL Stats", () -> assertEquals(this.url.getOriginalUrl(), response.originalUrl()),
                () -> assertEquals(this.url.getShortUrl(), response.shortUrl()));
    }

    @Test
    void testGetUrlStatsNotFound() {
        when(this.urlRepository.findByShortUrlAndDeletedFalse(anyString())).thenReturn(Optional.empty());

        assertThrows(UrlNotFoundException.class, () -> this.urlService.getUrlStats(UrlServiceTest.SHORT_URL));
    }
}
