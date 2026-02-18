package com.kubuski.urlshortener.mapper;

import com.kubuski.urlshortener.dto.UrlResponse;
import com.kubuski.urlshortener.dto.UserResponse;
import com.kubuski.urlshortener.entity.Url;
import com.kubuski.urlshortener.entity.User;

/**
 * Utility class for mapping between entities and DTOs.
 * Centralizes conversion logic to reduce code duplication.
 */
public final class EntityMapper {

    private EntityMapper() {
        // Utility class, prevent instantiation
    }

    /**
     * Converts a User entity to UserResponse DTO.
     *
     * @param user the User entity
     * @return UserResponse DTO
     */
    public static UserResponse toUserResponse(User user) {
        return new UserResponse(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getRole()
        );
    }

    /**
     * Converts a Url entity to UrlResponse DTO.
     *
     * @param url the Url entity
     * @return UrlResponse DTO
     */
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
