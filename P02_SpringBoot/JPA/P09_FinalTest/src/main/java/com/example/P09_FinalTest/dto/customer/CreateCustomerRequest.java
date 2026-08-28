package com.example.P09_FinalTest.dto.customer;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record CreateCustomerRequest(
        @NotBlank String fullName,
        @NotBlank @Email String email,
        String phone
) {
}
