package com.kubuski.urlshortener.service;

import com.kubuski.urlshortener.entity.User;
import io.jsonwebtoken.Claims;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class JwtServiceTest {

    private static final String SECRET_TEST_KEY = "1kp1Wv/9kfKl2Prat4lteX4T+DYR3bKcJ1BOy5sZrgg=";
    private static final String USERNAME = "testuser";
    private static final String EMAIL = "testuser@example.com";

    private String testToken;

    @InjectMocks
    private JwtService jwtService;

    @Mock
    private User user;

    @BeforeEach
    public void setUp() {
        ReflectionTestUtils.setField(jwtService, "secretKey", SECRET_TEST_KEY);

        when(user.getUsername()).thenReturn(USERNAME);
        when(user.getEmail()).thenReturn(EMAIL);

        testToken = jwtService.generateToken(user);
    }

    @Test
    void testExtractSubject() {
        String subject = jwtService.extractSubject(testToken);

        assertEquals(USERNAME, subject);
    }

    @Test
    void testExtractEmail() {
        String email = jwtService.extractEmail(testToken);

        assertEquals(EMAIL, email);
    }

    @Test
    void testIsTokenValid() {
        boolean validToken = jwtService.isTokenValid(testToken, user);

        assertTrue(validToken);
    }

    @Test
    void testGenerateToken() {
        String token = jwtService.generateToken(user);

        assertAll("Generate JWT", () -> assertEquals(USERNAME, jwtService.extractSubject(token)),
                () -> assertEquals(EMAIL, jwtService.extractEmail(token)));
    }

    @Test
    void testExtractAllClaims() {
        Claims claims = jwtService.extractAllClaims(testToken);

        assertAll("Extract JWT claims", () -> assertEquals(USERNAME, claims.getSubject()),
                () -> assertEquals(EMAIL, claims.get("email", String.class)));
    }

    @Test
    void testIsTokenExpired() {
        boolean tokenStatus = jwtService.isTokenExpired(testToken);

        assertFalse(tokenStatus);
    }
}
