package com.kubuski.urlshortener.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
public abstract class BaseException extends RuntimeException {
    protected BaseException(final String message) {
        super(message);
    }
}
