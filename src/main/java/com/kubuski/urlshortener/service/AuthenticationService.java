package com.kubuski.urlshortener.service;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.kubuski.urlshortener.dto.AuthenticationRequest;
import com.kubuski.urlshortener.dto.AuthenticationResponse;
import com.kubuski.urlshortener.dto.RegisterRequest;
import com.kubuski.urlshortener.entity.User;
import com.kubuski.urlshortener.repository.UserRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public AuthenticationResponse register(RegisterRequest request) {
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
}
