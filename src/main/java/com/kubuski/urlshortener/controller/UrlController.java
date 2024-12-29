package com.kubuski.urlshortener.controller;

import com.kubuski.urlshortener.dto.UrlRequest;
import com.kubuski.urlshortener.dto.UrlResponse;
import com.kubuski.urlshortener.service.UrlService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/shorten")
final class UrlController {

    private final UrlService urlService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UrlResponse createShortUrl(@Valid @RequestBody final UrlRequest urlRequest) {
        return this.urlService.createShortUrl(urlRequest);
    }

    @GetMapping("/{shortUrl}")
    @ResponseStatus(HttpStatus.OK)
    public UrlResponse getOriginalUrl(@PathVariable final String shortUrl) {
        return this.urlService.getOriginalUrl(shortUrl);
    }

    @PutMapping("/{shortUrl}")
    @ResponseStatus(HttpStatus.OK)
    public UrlResponse updateOriginalUrl(@PathVariable final String shortUrl,
            @RequestBody final UrlRequest urlRequest) {
        return this.urlService.updateOriginalUrl(shortUrl, urlRequest);
    }

    @DeleteMapping("/{shortUrl}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteUrl(@PathVariable final String shortUrl) {
        this.urlService.deleteUrl(shortUrl);
    }

    @GetMapping("/{shortUrl}/stats")
    @ResponseStatus(HttpStatus.OK)
    public UrlResponse getUrlStats(@PathVariable final String shortUrl) {
        return this.urlService.getUrlStats(shortUrl);
    }
}
