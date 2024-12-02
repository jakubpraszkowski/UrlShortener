package com.kubuski.urlshortener.service;

import org.springframework.stereotype.Service;
import com.kubuski.urlshortener.entity.User;
import com.kubuski.urlshortener.exception.UserNotFoundException;
import com.kubuski.urlshortener.repository.UserRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public User findByUsername(String username) {
        return userRepository.findByUsername(username).orElseThrow(
                () -> new UserNotFoundException("User " + username + " not found in Database"));
    }
}
