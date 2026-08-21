package com.example.P05_RestAPI.dto;

import com.example.P05_RestAPI.domain.UserStatus;

import java.time.Instant;

public record UserResponse(
        Long id,
        String name,
        String email,
        UserStatus status,
        Instant createdAt,
        Instant updatedAt
) {
}
