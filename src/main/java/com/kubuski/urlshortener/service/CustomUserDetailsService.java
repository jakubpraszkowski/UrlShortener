package com.kubuski.urlshortener.service;

import com.kubuski.urlshortener.entity.User;
import com.kubuski.urlshortener.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public User loadUserByUsername(String login) throws UsernameNotFoundException {
        return userRepository.findByEmail(login).orElseThrow(() -> new UsernameNotFoundException(
                "User not found with email or username: " + login));
    }
}
