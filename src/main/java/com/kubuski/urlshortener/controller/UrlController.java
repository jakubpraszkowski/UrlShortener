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

@RestController
@RequestMapping("/api/shorten")
final class UrlController {

    private final UrlService urlService;

    public UrlController(UrlService urlService) {
        this.urlService = urlService;
    }

    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    public UrlResponse createShortUrl(@Valid @RequestBody UrlRequest urlRequest) {
        return urlService.createShortUrl(urlRequest);
    }

    @GetMapping("/{shortUrl}")
    @ResponseStatus(HttpStatus.OK)
    public UrlResponse getOriginalUrl(@PathVariable(name = "shortUrl") String shortUrl) {
        return urlService.getOriginalUrl(shortUrl);
    }

    @PutMapping("/{shortUrl}")
    @ResponseStatus(HttpStatus.OK)
    public UrlResponse updateOriginalUrl(@PathVariable(name = "shortUrl") String shortUrl,
            @RequestBody UrlRequest urlRequest) {
        return urlService.updateOriginalUrl(shortUrl, urlRequest);
    }

    @DeleteMapping("/{shortUrl}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteUrl(@PathVariable(name = "shortUrl") String shortUrl) {
        urlService.deleteUrl(shortUrl);
    }

    @GetMapping("/{shortUrl}/stats")
    @ResponseStatus(HttpStatus.OK)
    public UrlResponse getUrlStats(@PathVariable(name = "shortUrl") String shortUrl) {
        return urlService.getUrlStats(shortUrl);
    }
}