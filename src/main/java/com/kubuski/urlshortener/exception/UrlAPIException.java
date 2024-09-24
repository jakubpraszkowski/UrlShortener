package com.kubuski.urlshortener.exception;

import org.springframework.http.HttpStatus;

public class UrlAPIException extends RuntimeException {
    private HttpStatus status;
    private String message;

    public UrlAPIException(HttpStatus status, String message) {
        this.status = status;
        this.message = message;
    }

    public UrlAPIException(String message, HttpStatus status, String message1) {
        super(message);
        this.status = status;
        this.message = message1;
    }

    public String getMessage() {
        return message;
    }

    public HttpStatus getStatus() {
        return status;
    }
}
