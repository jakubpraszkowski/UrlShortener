package com.kubuski.urlshortener.service;

import com.kubuski.urlshortener.dto.AuthenticationRequest;
import com.kubuski.urlshortener.dto.AuthenticationResponse;
import com.kubuski.urlshortener.dto.RegisterRequest;
import com.kubuski.urlshortener.entity.User;
import com.kubuski.urlshortener.exception.UserAlreadyExistsException;
import com.kubuski.urlshortener.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    @Transactional
    public AuthenticationResponse register(RegisterRequest request) {
        checkIfUserExists(request.email(), request.username());

        User user = createUser(request);
        userRepository.save(user);
        String jwtToken = jwtService.generateToken(user);

        return new AuthenticationResponse(jwtToken);
    }

    @Transactional
    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        authenticateUser(request);
        User user = findUserByLogin(request.login());
        String jwtToken = jwtService.generateToken(user);

        return new AuthenticationResponse(jwtToken);
    }

    private User createUser(RegisterRequest request) {
        return User.builder().username(request.username())
                .password(passwordEncoder.encode(request.password())).email(request.email())
                .role(request.role()).build();
    }

    private void authenticateUser(AuthenticationRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.login(), request.password()));
    }

    private User findUserByLogin(String login) {
        return userRepository.findByEmailOrUsername(login)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + login));
    }

    private void checkIfUserExists(String email, String username) {
        if (userExists(email, username)) {
            throw new UserAlreadyExistsException(
                    "User with email " + email + " or username " + username + " already exists"
            );
        }
    }

    private boolean userExists(String email, String username) {
        return userRepository.existsByEmailOrUsername(email, username);
    }
}
