package com.example.P09_FinalTest.dto.customer;

import com.example.P09_FinalTest.entity.enums.CustomerStatus;

import java.time.LocalDateTime;

public record CustomerResponse(
        Long id,
        String fullName,
        String email,
        String phone,
        CustomerStatus status,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
}
