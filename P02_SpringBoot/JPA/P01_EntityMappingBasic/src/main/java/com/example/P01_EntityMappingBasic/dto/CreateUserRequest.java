package com.example.P01_EntityMappingBasic.dto;

import com.example.P01_EntityMappingBasic.enums.UserRole;
import com.example.P01_EntityMappingBasic.enums.UserStatus;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

public record CreateUserRequest(
        @NotBlank @Size(max = 100) String fullName,
        @NotBlank @Email @Size(max = 150) String email,
        @NotNull UserStatus status,
        @NotNull UserRole role,
        LocalDate dateOfBirth,
        @NotNull Boolean active,
        @PositiveOrZero Integer age,
        String temporaryDisplayName
) {
}
