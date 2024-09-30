package com.kubuski.urlshortener.service;

import org.springframework.stereotype.Service;

import com.kubuski.urlshortener.dto.UrlDto;
import com.kubuski.urlshortener.entity.Url;
import com.kubuski.urlshortener.repository.UrlRepository;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Random;
import lombok.extern.log4j.Log4j2;

@Service
@Log4j2
public class UrlService{
    private final UrlRepository urlRepository;

    public UrlService(UrlRepository urlRepository) {
        this.urlRepository = urlRepository;
    }

    public UrlDto createShortUrl(UrlDto originalUrl) {
        Url url = new Url();
        url.setOriginalUrl(originalUrl.getOriginalUrl());
        url.setShortUrl(generateShortCode());
        url.setCreatedAt(LocalDateTime.now());
        url.setUpdatedAt(LocalDateTime.now());
        url.setAccessCount(0);

        urlRepository.save(url);

        log.info(originalUrl + " shortened to " + url.getShortUrl());

        return convertToDto(url);
    }

    public UrlDto getOriginalUrl(UrlDto shortUrl) {
        log.info("Accessing short URL: " + shortUrl);

        Optional<Url> url = urlRepository.findByShortUrl(shortUrl);

        Url urlEntity = url.orElseThrow(() -> {
            log.error("Short URL not found: " + shortUrl);
            return new RuntimeException("Url " + shortUrl + " not found");
        });

        return convertToDto(urlEntity);
    }

    public boolean deleteUrl(UrlDto shortUrl) {
        Optional<Url> url = urlRepository.findByShortUrl(shortUrl);

        if (!url.isPresent()) {
            return false;
        }

        urlRepository.delete(url.get());
        return true;
    }

    private String generateShortCode() {
        Random random = new Random();

        StringBuilder shortCode = new StringBuilder();
        for (int i = 0; i < 6; i++) {
            shortCode.append((char) (random.nextInt(26) + 'a'));
        }
        return shortCode.toString();
    }

    public UrlDto convertToDto(Url url) {
        UrlDto urlDto = new UrlDto();
        urlDto.setId(url.getId());
        urlDto.setOriginalUrl(url.getOriginalUrl());
        urlDto.setShortUrl(url.getShortUrl());
        urlDto.setCreatedAt(url.getCreatedAt());
        urlDto.setUpdateAt(url.getUpdatedAt());
        urlDto.setExpirationDate(url.getExpirationDate());
        urlDto.setAccessCount(url.getAccessCount());

        return urlDto;
    }

    public Url convertToEntity(UrlDto urlDto) {

        Url url = new Url();

        url.setOriginalUrl(urlDto.getOriginalUrl());

        url.setShortUrl(urlDto.getShortUrl());

        return url;

    }
}
