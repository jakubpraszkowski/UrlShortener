package com.kubuski.urlshortener.dto;

import com.kubuski.urlshortener.entity.Roles;

public record UserResponse(Long id, String username, String email, Roles role) {

}
