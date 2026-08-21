package com.example.P06_RestController.dto;

import com.example.P06_RestController.domain.UserStatus;

// Request DTO: full JSON body for PUT /api/users/{id}.
public record UpdateUserRequest(
        String name,
        String email,
        UserStatus status
) {
}
