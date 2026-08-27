package com.example.P04_EntityRelationships.enrollment.dto;

import java.time.LocalDateTime;

public record EnrollmentResponse(
        Long id,
        Long studentId,
        Long courseId,
        LocalDateTime enrolledAt,
        String status,
        String grade
) {
}
