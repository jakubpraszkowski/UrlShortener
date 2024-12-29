package com.kubuski.urlshortener.controller;

import com.kubuski.urlshortener.dto.UserRequest;
import com.kubuski.urlshortener.dto.UserResponse;
import com.kubuski.urlshortener.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/users")
final class UserController {

    private final UserService userService;

    @GetMapping("/{email}")
    @ResponseStatus(HttpStatus.OK)
    public UserResponse getUserByEmail(@PathVariable String email) {
        return userService.findByEmail(email);
    }

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public UserResponse registerUser(@RequestBody UserRequest userRequest) {
        return userService.registerUser(userRequest);
    }
}
