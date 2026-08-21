package com.example.P06_RestController.dto;

import com.example.P06_RestController.domain.UserStatus;

import java.time.Instant;

// Response DTO: API response does not expose the internal User object directly.
public record UserResponse(
        Long id,
        String name,
        String email,
        UserStatus status,
        Instant createdAt,
        Instant updatedAt
) {
}
