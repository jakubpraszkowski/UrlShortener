package com.kubuski.urlshortener.controller;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import com.kubuski.urlshortener.dto.UrlRequest;
import com.kubuski.urlshortener.dto.UrlResponse;
import com.kubuski.urlshortener.service.UrlService;

@ExtendWith(MockitoExtension.class)
public class UrlControllerTest {

    private static final String SHORT_URL = "shortUrl";
    private static final String ORIGINAL_URL = "https://example.com";
    private static final String NEW_URL = "https://newexample.com";

    @Mock
    private UrlService urlService;

    @InjectMocks
    private UrlController urlController;

    private UrlRequest urlRequest;
    private UrlResponse urlResponse;

    @BeforeEach
    public void setUp() {
        this.urlResponse = new UrlResponse(1L, UrlControllerTest.ORIGINAL_URL, UrlControllerTest.SHORT_URL, null, null, null, 0);
        this.urlRequest = new UrlRequest(UrlControllerTest.ORIGINAL_URL);
    }

    @Test
    public void testCreateShortUrl() {
        when(this.urlService.createShortUrl(any(UrlRequest.class))).thenReturn(this.urlResponse);

        final UrlResponse response = this.urlController.createShortUrl(this.urlRequest);

        assertAll(() -> assertEquals(this.urlRequest.url(), response.originalUrl()),
                () -> assertEquals(this.urlResponse.shortUrl(), response.shortUrl()));
    }

    @Test
    public void testGetOriginalUrl() {
        when(this.urlService.getOriginalUrl(anyString())).thenReturn(this.urlResponse);

        final UrlResponse response = this.urlController.getOriginalUrl(UrlControllerTest.SHORT_URL);

        assertAll(() -> assertEquals(this.urlResponse.originalUrl(), response.originalUrl()),
                () -> assertEquals(this.urlResponse.shortUrl(), response.shortUrl()));
    }

    @Test
    public void testUpdateOriginalUrl() {
        final UrlRequest newUrlRequest = new UrlRequest(UrlControllerTest.NEW_URL);
        final UrlResponse updatedUrlResponse =
                new UrlResponse(1L, UrlControllerTest.NEW_URL, UrlControllerTest.SHORT_URL, null, null, null, 0);
        when(this.urlService.updateOriginalUrl(any(String.class), any(UrlRequest.class)))
                .thenReturn(updatedUrlResponse);

        final UrlResponse response = this.urlController.updateOriginalUrl(UrlControllerTest.SHORT_URL, newUrlRequest);

        assertAll(() -> assertEquals(updatedUrlResponse.shortUrl(), response.shortUrl()),
                () -> assertEquals(newUrlRequest.url(), response.originalUrl()));
    }

    @Test
    public void testDeleteUrl() {
        doNothing().when(this.urlService).deleteUrl(UrlControllerTest.SHORT_URL);

        this.urlController.deleteUrl(UrlControllerTest.SHORT_URL);
    }

    @Test
    public void testGetStatsUrl() {
        when(this.urlService.getUrlStats(UrlControllerTest.SHORT_URL)).thenReturn(this.urlResponse);

        final UrlResponse response = this.urlController.getUrlStats(UrlControllerTest.SHORT_URL);

        assertAll(() -> assertEquals(this.urlResponse.originalUrl(), response.originalUrl()),
                () -> assertEquals(this.urlResponse.shortUrl(), response.shortUrl()));
    }
}
