package com.kubuski.urlshortener.dto;

import com.kubuski.urlshortener.entity.Roles;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record RegisterRequest(
        @NotBlank(message = "Username is required") String username,
        @NotBlank(message = "Password is required") String password,
        @NotBlank(message = "Email is required") @Email(message = "Email should be valid") String email,
        @NotNull(message = "Role is required") Roles role) {

}
