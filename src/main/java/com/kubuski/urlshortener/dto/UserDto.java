package com.kubuski.urlshortener.dto;

import com.kubuski.urlshortener.entity.Roles;

public record UserDto(Long id, String username, String password, Roles role) {

}
