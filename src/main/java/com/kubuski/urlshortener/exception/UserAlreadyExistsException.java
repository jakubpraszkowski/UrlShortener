package com.kubuski.urlshortener.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class UserAlreadyExistsException extends BaseException {
    public UserAlreadyExistsException(String message) {
        super(message);
    }
}
