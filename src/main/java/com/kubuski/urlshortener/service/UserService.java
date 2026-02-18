package com.kubuski.urlshortener.service;

import com.kubuski.urlshortener.dto.UserRequest;
import com.kubuski.urlshortener.dto.UserResponse;
import com.kubuski.urlshortener.entity.User;
import com.kubuski.urlshortener.exception.UserNotFoundException;
import com.kubuski.urlshortener.mapper.EntityMapper;
import com.kubuski.urlshortener.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional(readOnly = true)
    public UserResponse findByEmail(String email) {
        User user = userRepository.findByEmail(email).orElseThrow(
                () -> new UserNotFoundException("User " + email + " not found in Database"));

        return EntityMapper.toUserResponse(user);
    }

    public UserResponse registerUser(UserRequest userRequest) {
        User user = toUser(userRequest);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);

        return EntityMapper.toUserResponse(user);
    }

    private User toUser(UserRequest userRequest) {
        return User.builder().username(userRequest.username()).password(userRequest.password())
                .email(userRequest.email()).role(userRequest.role()).build();
    }
}
