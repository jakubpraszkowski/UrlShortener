package com.kubuski.urlshortener.dto;

import com.kubuski.urlshortener.entity.Roles;

public record UserRequest(String username, String password, String email, Roles role) {

}
