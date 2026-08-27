package com.example.P04_EntityRelationships.enrollment.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CreateEnrollmentStudentRequest(
        @NotBlank @Size(max = 100) String name
) {
}
