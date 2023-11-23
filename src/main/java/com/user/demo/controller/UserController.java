package com.user.demo.controller;

import com.user.demo.dto.UserDTO;
import com.user.demo.dto.UserResponseDTO;
import com.user.demo.exceptions.CustomException;
import com.user.demo.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;

@RestController
@RequestMapping("/api/users")
public class UserController {
    private final UserService userService;
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public ResponseEntity<?> createUser(@RequestBody @Valid UserDTO userDto) {
        try {
            UserResponseDTO newUser = userService.registerUser(userDto);
            return ResponseEntity.status(HttpStatus.CREATED).body(newUser);
        } catch (CustomException e) {
            return ResponseEntity
                    .status(e.getHttpStatus())
                    .body(Collections.singletonMap("mensaje", e.getMessage()));
        }
    }
}

