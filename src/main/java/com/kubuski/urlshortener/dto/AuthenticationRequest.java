package com.kubuski.urlshortener.dto;

import jakarta.validation.constraints.NotBlank;

public record AuthenticationRequest(
        @NotBlank(message = "Login is required") String login,
        @NotBlank(message = "Password is required") String password) {

}
