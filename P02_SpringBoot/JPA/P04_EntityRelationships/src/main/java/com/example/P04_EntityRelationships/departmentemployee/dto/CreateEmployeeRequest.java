package com.example.P04_EntityRelationships.departmentemployee.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CreateEmployeeRequest(
        @NotBlank @Size(max = 100) String name
) {
}
