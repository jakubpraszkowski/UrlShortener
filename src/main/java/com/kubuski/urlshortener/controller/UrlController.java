package com.kubuski.urlshortener.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kubuski.urlshortener.dto.UrlDto;
import com.kubuski.urlshortener.service.UrlService;

@RestController
@RequestMapping("/shorten")
public class UrlController {
    private final UrlService urlService;

    public UrlController(UrlService urlService) {
        this.urlService = urlService;
    }

    @PostMapping
    public ResponseEntity<UrlDto> createShortUrl(@RequestBody UrlDto urlRequest) {
        UrlDto url = urlService.createShortUrl(urlRequest.getOriginalUrl());
        return ResponseEntity.ok(url);
    }

    @GetMapping("/{shortUrl}")
    public ResponseEntity<UrlDto> getOriginalUrl(@PathVariable String shortUrl) {
        return ResponseEntity.ok(urlService.getOriginalUrl(shortUrl).orElseThrow(() -> 
        {
            log.error("Short URL not found: " + shortUrl);
            return new RuntimeException("Url " + shortUrl + " not found");
        }));
    }

    @DeleteMapping("/{shortUrl}")
    public ResponseEntity<UrlDto> deleteUrl(@PathVariable String shortUrl) {
        if (!urlService.deleteUrl(shortUrl)) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok().build();
    }
}