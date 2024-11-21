package com.kubuski.urlshortener.dto;

import jakarta.validation.constraints.NotEmpty;

public record UrlRequest(@NotEmpty String url) {
}