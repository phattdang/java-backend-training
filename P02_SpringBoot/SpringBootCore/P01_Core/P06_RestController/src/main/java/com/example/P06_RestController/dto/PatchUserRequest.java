package com.example.P06_RestController.dto;

import com.example.P06_RestController.domain.UserStatus;

// Request DTO: partial JSON body for PATCH /api/users/{id}.
public record PatchUserRequest(
        String name,
        String email,
        UserStatus status
) {
}
