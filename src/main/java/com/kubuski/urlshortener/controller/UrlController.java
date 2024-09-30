package com.kubuski.urlshortener.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kubuski.urlshortener.dto.UrlDto;
import com.kubuski.urlshortener.entity.Url;
import com.kubuski.urlshortener.repository.UrlRepository;
import com.kubuski.urlshortener.service.UrlService;

@RestController
@RequestMapping("/api/shorten")
public class UrlController {

    private final UrlService urlService;
    private final UrlRepository urlRepository;

    public UrlController(UrlRepository urlRepository, UrlService urlService) {
        this.urlRepository = urlRepository;
        this.urlService = urlService;
    }

    @PostMapping
    public ResponseEntity<UrlDto> createShortUrl(@RequestBody UrlDto urlRequest) {
        return new ResponseEntity<>(urlService.createShortUrl(urlRequest), HttpStatus.CREATED);
    }

    // @PostMapping
    // public UrlDto createShortUrl(@RequestBody UrlDto urlDto) {
    //     Url url = urlService.convertToEntity(urlDto);
    //     Url savedUrl = urlRepository.save(url);
        
    //     return urlService.convertToDto(savedUrl);
    // }

    @GetMapping("/{shortUrl}")
    public ResponseEntity<UrlDto> getOriginalUrl(@PathVariable UrlDto shortUrl) {
        return ResponseEntity.ok(urlService.getOriginalUrl(shortUrl));
    }

    @DeleteMapping("/{shortUrl}")
    public ResponseEntity<UrlDto> deleteUrl(@PathVariable UrlDto shortUrl) {
        if (!urlService.deleteUrl(shortUrl)) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok().build();
    }

    @GetMapping("/health")
    public ResponseEntity<String> healthCheck() {
        return ResponseEntity.ok("Application is running");
    }
}