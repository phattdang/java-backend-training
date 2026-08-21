package com.example.P08_ExceptionHandling.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

public record CreateUserRequest(
        @NotBlank(message = "fullName must not be blank")
        String fullName,

        @NotBlank(message = "email must not be blank")
        @Email(message = "email must be valid")
        String email,

        @Min(value = 18, message = "age must be at least 18")
        int age
) {
}
