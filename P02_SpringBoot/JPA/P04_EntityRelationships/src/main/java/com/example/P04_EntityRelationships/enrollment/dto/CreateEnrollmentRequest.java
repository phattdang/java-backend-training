package com.example.P04_EntityRelationships.enrollment.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;

public record CreateEnrollmentRequest(
        @NotNull Long studentId,
        @NotNull Long courseId,
        LocalDateTime enrolledAt,
        @NotBlank @Size(max = 30) String status,
        @Size(max = 10) String grade
) {
}
