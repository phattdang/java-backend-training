package com.example.P04_EntityRelationships.userprofile.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CreateProfileRequest(
        @NotBlank @Size(max = 255) String bio
) {
}
