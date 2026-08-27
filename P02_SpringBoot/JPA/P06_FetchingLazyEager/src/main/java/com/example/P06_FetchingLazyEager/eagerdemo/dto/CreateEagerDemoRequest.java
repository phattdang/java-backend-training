package com.example.P06_FetchingLazyEager.eagerdemo.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

public record CreateEagerDemoRequest(
        @NotBlank @Size(max = 100) String departmentName,
        @NotBlank @Size(max = 100) String employeeName,
        @NotBlank @Email @Size(max = 150) String employeeEmail,
        @NotNull @PositiveOrZero BigDecimal salary
) {
}
