package com.kubuski.urlshortener.mapper;

import com.kubuski.urlshortener.dto.UrlResponse;
import com.kubuski.urlshortener.dto.UserResponse;
import com.kubuski.urlshortener.entity.Url;
import com.kubuski.urlshortener.entity.User;

public final class UserMapper {

    private UserMapper() {
    }

    public static UserResponse toUserResponse(User user) {
        return new UserResponse(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getRole()
        );
    }

    public static UrlResponse toUrlResponse(Url url) {
        return new UrlResponse(
                url.getId(),
                url.getOriginalUrl(),
                url.getShortUrl(),
                url.getCreatedAt(),
                url.getUpdatedAt(),
                url.getExpirationDate(),
                url.getAccessCount()
        );
    }
}
