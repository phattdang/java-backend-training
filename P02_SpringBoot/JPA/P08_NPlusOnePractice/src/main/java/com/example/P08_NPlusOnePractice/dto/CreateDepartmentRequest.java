package com.example.P08_NPlusOnePractice.dto;

import jakarta.validation.constraints.NotBlank;

public record CreateDepartmentRequest(
        @NotBlank String name
) {
}
