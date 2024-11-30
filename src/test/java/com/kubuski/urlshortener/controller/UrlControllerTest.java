package com.kubuski.urlshortener.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import com.kubuski.urlshortener.dto.UrlRequest;
import com.kubuski.urlshortener.dto.UrlResponse;
import com.kubuski.urlshortener.service.UrlService;

public class UrlControllerTest {

    @Mock
    private UrlService urlService;

    @InjectMocks
    private UrlController urlController;

    private UrlResponse urlResponse;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        urlResponse = new UrlResponse(1L, "https://example.com", "shortUrl", null, null, null, 0);
    }

    @Test
    public void testCreateShortUrl() {
        // given
        UrlRequest urlRequest = new UrlRequest("https://example.com");
        when(urlService.createShortUrl(any(UrlRequest.class))).thenReturn(urlResponse);

        // when
        UrlResponse response = urlController.createShortUrl(urlRequest);

        // then
        assertEquals(urlRequest.url(), response.originalUrl());
        assertEquals(urlResponse.shortUrl(), response.shortUrl());
    }

    @Test
    public void testGetOriginalUrl() {
        // given
        String shortUrl = "shortUrl";
        when(urlService.getOriginalUrl(anyString())).thenReturn(urlResponse);

        // when
        UrlResponse response = urlController.getOriginalUrl(shortUrl);

        // then
        assertEquals(urlResponse.originalUrl(), response.originalUrl());
        assertEquals(urlResponse.shortUrl(), response.shortUrl());
    }

    @Test
    public void testUpdateOriginalUrl() {
        // given
        UrlRequest urlRequest = new UrlRequest("https://newexample.com");
        UrlResponse updatedUrlResponse =
                new UrlResponse(1L, "https://newexample.com", "shortUrl", null, null, null, 0);
        when(urlService.updateOriginalUrl(any(String.class), any(UrlRequest.class)))
                .thenReturn(updatedUrlResponse);

        // when
        UrlResponse response = urlController.updateOriginalUrl("shortUrl", urlRequest);

        // then
        assertEquals(updatedUrlResponse.shortUrl(), response.shortUrl());
        assertEquals(urlRequest.url(), response.originalUrl());
    }

    @Test
    public void testDeleteUrl() {
        // given
        String shortUrl = "shortUrl";
        doNothing().when(urlService).deleteUrl(shortUrl);

        // when
        urlController.deleteUrl(shortUrl);

        // then
    }

    @Test
    public void testGetStatsUrl() {
        // given
        String shortUrl = "shortUrl";
        when(urlService.getUrlStats(shortUrl)).thenReturn(urlResponse);

        // when
        UrlResponse response = urlController.getUrlStats(shortUrl);

        // then
        assertEquals(urlResponse.originalUrl(), response.originalUrl());
        assertEquals(urlResponse.shortUrl(), response.shortUrl());
    }
}
