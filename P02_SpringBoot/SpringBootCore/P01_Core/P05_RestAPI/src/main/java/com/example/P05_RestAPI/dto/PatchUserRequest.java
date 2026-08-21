package com.example.P05_RestAPI.dto;

import com.example.P05_RestAPI.domain.UserStatus;

public record PatchUserRequest(
        String name,
        String email,
        UserStatus status
) {
}
