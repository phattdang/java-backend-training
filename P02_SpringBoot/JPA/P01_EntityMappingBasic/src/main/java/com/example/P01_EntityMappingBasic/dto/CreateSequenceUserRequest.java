package com.example.P01_EntityMappingBasic.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CreateSequenceUserRequest(
        @NotBlank @Size(max = 100) String name
) {
}
