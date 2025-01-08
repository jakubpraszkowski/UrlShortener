package com.kubuski.urlshortener.controller;

import com.kubuski.urlshortener.dto.UrlRequest;
import com.kubuski.urlshortener.dto.UrlResponse;
import com.kubuski.urlshortener.service.UrlService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

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
        urlResponse = new UrlResponse(1L, ORIGINAL_URL, SHORT_URL, null, null, null, 0);
        urlRequest = new UrlRequest(ORIGINAL_URL);
    }

    @Test
    public void testCreateShortUrl() {
        when(urlService.createShortUrl(any(UrlRequest.class))).thenReturn(urlResponse);

        UrlResponse response = urlController.createShortUrl(urlRequest);

        assertAll(() -> assertEquals(urlRequest.url(), response.originalUrl()),
                () -> assertEquals(urlResponse.shortUrl(), response.shortUrl()));
    }

    @Test
    public void testGetOriginalUrl() {
        when(urlService.getOriginalUrl(anyString())).thenReturn(urlResponse);

        UrlResponse response = urlController.getOriginalUrl(SHORT_URL);

        assertAll(() -> assertEquals(urlResponse.originalUrl(), response.originalUrl()),
                () -> assertEquals(urlResponse.shortUrl(), response.shortUrl()));
    }

    @Test
    public void testUpdateOriginalUrl() {
        UrlRequest newUrlRequest = new UrlRequest(NEW_URL);
        UrlResponse updatedUrlResponse =
                new UrlResponse(1L, NEW_URL, SHORT_URL, null, null, null, 0);
        when(urlService.updateOriginalUrl(any(String.class), any(UrlRequest.class)))
                .thenReturn(updatedUrlResponse);

        UrlResponse response = urlController.updateOriginalUrl(SHORT_URL, newUrlRequest);

        assertAll(() -> assertEquals(updatedUrlResponse.shortUrl(), response.shortUrl()),
                () -> assertEquals(newUrlRequest.url(), response.originalUrl()));
    }

    @Test
    public void testDeleteUrl() {
        doNothing().when(urlService).deleteUrl(SHORT_URL);

        urlController.deleteUrl(SHORT_URL);
    }

    @Test
    public void testGetStatsUrl() {
        when(urlService.getUrlStats(SHORT_URL)).thenReturn(urlResponse);

        UrlResponse response = urlController.getUrlStats(SHORT_URL);

        assertAll(() -> assertEquals(urlResponse.originalUrl(), response.originalUrl()),
                () -> assertEquals(urlResponse.shortUrl(), response.shortUrl()));
    }
}
