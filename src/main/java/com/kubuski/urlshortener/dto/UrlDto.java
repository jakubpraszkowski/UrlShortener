package com.kubuski.urlshortener.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class UrlDto {
    private Long id;
    private String originalUrl;
    private String shortUrl;
    private LocalDateTime createdAt;
    private LocalDateTime updateAt;
    private LocalDateTime expirationDate;
    private int accessCount;
}
