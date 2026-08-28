package com.example.P08_NPlusOnePractice.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record CreateEmployeeRequest(
        @NotBlank String name,
        @NotBlank @Email String email,
        @NotNull @DecimalMin("0.0") BigDecimal salary,
        @NotNull Long departmentId
) {
}
