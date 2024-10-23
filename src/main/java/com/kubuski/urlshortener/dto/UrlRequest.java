package com.kubuski.urlshortener.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class UrlRequest {
    @NotEmpty(message = "URL cannot be empty")
    private String url;
}
