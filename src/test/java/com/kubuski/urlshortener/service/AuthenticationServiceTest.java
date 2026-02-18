package com.kubuski.urlshortener.service;

import com.kubuski.urlshortener.dto.AuthenticationRequest;
import com.kubuski.urlshortener.dto.AuthenticationResponse;
import com.kubuski.urlshortener.dto.RegisterRequest;
import com.kubuski.urlshortener.entity.Roles;
import com.kubuski.urlshortener.entity.User;
import com.kubuski.urlshortener.exception.UserAlreadyExistsException;
import com.kubuski.urlshortener.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AuthenticationServiceTest {
    @Mock
    private UserRepository userRepository;

    @Mock
    private JwtService jwtService;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private AuthenticationManager authenticationManager;

    @InjectMocks
    private AuthenticationService authenticationService;

    private User user;

    @BeforeEach
    public void setUp() {
        user = User.builder().username("testuser").password(passwordEncoder.encode("testpassword")).email("testuser@example.com").role(Roles.USER).build();
    }

    @Test
    public void testRegisterUser() {
        RegisterRequest request = new RegisterRequest("testuser", "testpassword", "testuser@example.com", Roles.USER);
        when(userRepository.save(any(User.class))).thenReturn(user);
        when(jwtService.generateToken(user)).thenReturn("mockJwtToken");

        AuthenticationResponse response = authenticationService.register(request);

        assertNotNull(response);
        assertEquals("mockJwtToken", response.token());
    }

    @Test
    public void testRegisterUserAlreadyExists() {
        RegisterRequest request = new RegisterRequest("testuser", "testpassword", "testuser@example.com", Roles.USER);

        when(userRepository.existsByEmailOrUsername(request.email(), request.username())).thenReturn(true);

        assertThrows(UserAlreadyExistsException.class, () -> {
            authenticationService.register(request);
        });
    }

    @Test
    public void testAuthenticateUser() {
        AuthenticationRequest request = new AuthenticationRequest("testuser", "testpassword");
        when(userRepository.findByEmailOrUsername(request.login())).thenReturn(Optional.of(user));
        when(jwtService.generateToken(user)).thenReturn("mockJwtToken");

        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.login(), request.password()));
        AuthenticationResponse response = authenticationService.authenticate(request);

        assertNotNull(response);
        assertEquals("mockJwtToken", response.token());
    }
}
