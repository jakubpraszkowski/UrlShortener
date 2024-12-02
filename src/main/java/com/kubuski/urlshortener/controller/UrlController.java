package com.kubuski.urlshortener.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.kubuski.urlshortener.dto.UrlRequest;
import com.kubuski.urlshortener.dto.UrlResponse;
import com.kubuski.urlshortener.service.UrlService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/shorten")
final class UrlController {

    private final UrlService urlService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UrlResponse createShortUrl(@Valid @RequestBody UrlRequest urlRequest) {
        return urlService.createShortUrl(urlRequest);
    }

    @GetMapping("/{shortUrl}")
    @ResponseStatus(HttpStatus.OK)
    public UrlResponse getOriginalUrl(@PathVariable String shortUrl) {
        return urlService.getOriginalUrl(shortUrl);
    }

    @PutMapping("/{shortUrl}")
    @ResponseStatus(HttpStatus.OK)
    public UrlResponse updateOriginalUrl(@PathVariable String shortUrl,
            @RequestBody UrlRequest urlRequest) {
        return urlService.updateOriginalUrl(shortUrl, urlRequest);
    }

    @DeleteMapping("/{shortUrl}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteUrl(@PathVariable String shortUrl) {
        urlService.deleteUrl(shortUrl);
    }

    @GetMapping("/{shortUrl}/stats")
    @ResponseStatus(HttpStatus.OK)
    public UrlResponse getUrlStats(@PathVariable String shortUrl) {
        return urlService.getUrlStats(shortUrl);
    }
}
