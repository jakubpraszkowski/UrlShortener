package com.kubuski.urlshortener.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;
import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;

@ExtendWith(MockitoExtension.class)
public class JwtServiceTest {

    private static final String EXAMPLE_EMAIL = "test@example.com";

    // @InjectMocks
    private JwtService jwtService;

    @Mock
    private UserDetails userDetails;

    private String token;

    @BeforeEach
    public void setUp() {
        jwtService = new JwtService();
        when(userDetails.getUsername()).thenReturn(EXAMPLE_EMAIL);
        token = jwtService.generateToken(userDetails);
    }

    @Test
    public void testGenerateToken() {
        assertNotNull(token);
    }

    @Test
    public void testIsTokenValid() {
        String invalidToken = token + "invalid";

        assertTrue(jwtService.isTokenValid(token, userDetails));
        assertFalse(jwtService.isTokenValid(invalidToken, userDetails));
    }

    @Test
    public void testExtractEmail() {
        String email = jwtService.extractEmail(token);

        assertEquals(EXAMPLE_EMAIL, email);
    }

    // @Test
    // public void testIsTokenExpired() {
    //     assertFalse(jwtService.isTokenExpired(token));
    // }
}
