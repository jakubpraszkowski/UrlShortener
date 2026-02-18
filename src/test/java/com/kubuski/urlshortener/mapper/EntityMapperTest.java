package com.kubuski.urlshortener.mapper;

import com.kubuski.urlshortener.dto.UrlResponse;
import com.kubuski.urlshortener.dto.UserResponse;
import com.kubuski.urlshortener.entity.Roles;
import com.kubuski.urlshortener.entity.Url;
import com.kubuski.urlshortener.entity.User;
import org.junit.jupiter.api.Test;

import java.time.Instant;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for EntityMapper utility.
 */
public class EntityMapperTest {

    @Test
    public void testToUserResponse() {
        User user = User.builder()
                .id(1L)
                .username("testuser")
                .email("test@example.com")
                .role(Roles.USER)
                .password("hashedPassword")
                .build();

        UserResponse response = EntityMapper.toUserResponse(user);

        assertAll(
                () -> assertEquals(user.getId(), response.id()),
                () -> assertEquals(user.getUsername(), response.username()),
                () -> assertEquals(user.getEmail(), response.email()),
                () -> assertEquals(user.getRole(), response.role())
        );
    }

    @Test
    public void testToUrlResponse() {
        Instant now = Instant.now();
        Url url = Url.builder()
                .id(1L)
                .originalUrl("https://example.com")
                .shortUrl("abc123")
                .createdAt(now)
                .updatedAt(now)
                .expirationDate(null)
                .accessCount(5)
                .deleted(false)
                .build();

        UrlResponse response = EntityMapper.toUrlResponse(url);

        assertAll(
                () -> assertEquals(url.getId(), response.id()),
                () -> assertEquals(url.getOriginalUrl(), response.originalUrl()),
                () -> assertEquals(url.getShortUrl(), response.shortUrl()),
                () -> assertEquals(url.getCreatedAt(), response.createdAt()),
                () -> assertEquals(url.getUpdatedAt(), response.updatedAt()),
                () -> assertEquals(url.getExpirationDate(), response.expirationDate()),
                () -> assertEquals(url.getAccessCount(), response.accessCount())
        );
    }
}
