package com.kubuski.urlshortener.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class UrlIsNullException extends RuntimeException {
    public UrlIsNullException(final String message) {
        super(message);
    }
}
