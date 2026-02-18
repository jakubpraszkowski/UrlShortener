package com.kubuski.urlshortener.controller;

import com.kubuski.urlshortener.dto.UrlRequest;
import com.kubuski.urlshortener.dto.UrlResponse;
import com.kubuski.urlshortener.service.UrlService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/shorten")
@PreAuthorize("hasRole('USER')")
class UrlController {

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
            @Valid @RequestBody UrlRequest urlRequest) {
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
