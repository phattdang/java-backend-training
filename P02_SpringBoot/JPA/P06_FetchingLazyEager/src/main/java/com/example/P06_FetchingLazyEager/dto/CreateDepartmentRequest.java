package com.example.P06_FetchingLazyEager.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CreateDepartmentRequest(
        @NotBlank @Size(max = 100) String name,
        @NotBlank @Size(max = 500) String description
) {
}
