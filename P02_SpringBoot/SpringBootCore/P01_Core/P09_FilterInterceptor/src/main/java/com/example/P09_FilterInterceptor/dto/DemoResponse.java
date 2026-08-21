package com.example.P09_FilterInterceptor.dto;

import java.time.LocalDateTime;

public record DemoResponse(
        Long id,
        String name,
        Integer age,
        String requestId,
        String message,
        LocalDateTime timestamp
) {
}
