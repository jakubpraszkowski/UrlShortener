package com.kubuski.urlshortener.dto;

import com.kubuski.urlshortener.entity.Roles;

public record RegisterRequest(String username, String password, String email, Roles role) {

}
