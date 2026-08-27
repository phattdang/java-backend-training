package com.example.P05_JPQLCustomQuery.dto;

import java.math.BigDecimal;

public record AggregateResult(
        long employeeCount,
        BigDecimal totalSalary,
        Double averageSalary
) {
}
