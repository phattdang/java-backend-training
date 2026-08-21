package com.example.P08_ExceptionHandling.dto;

public record UserResponse(
        Long id,
        String fullName,
        String email,
        int age
) {
}
