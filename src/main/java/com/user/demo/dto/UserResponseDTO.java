package com.user.demo.dto;

import java.time.LocalDateTime;
import java.util.UUID;
public record UserResponseDTO(
        UUID id,
        String name,
        String email,
        LocalDateTime created,
        LocalDateTime modified,
        LocalDateTime lastLogin,
        String token,
        boolean isActive
) { }
