package com.example.P05_JPQLCustomQuery.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

import java.math.BigDecimal;

public record UpdateSalaryRequest(
        @NotNull @PositiveOrZero BigDecimal salary
) {
}
