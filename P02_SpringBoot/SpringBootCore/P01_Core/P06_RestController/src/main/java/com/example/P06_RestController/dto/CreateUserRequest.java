package com.example.P06_RestController.dto;

import com.example.P06_RestController.domain.UserStatus;

// Request DTO: JSON body for POST /api/users.
public record CreateUserRequest(
        String name,
        String email,
        UserStatus status
) {
}
