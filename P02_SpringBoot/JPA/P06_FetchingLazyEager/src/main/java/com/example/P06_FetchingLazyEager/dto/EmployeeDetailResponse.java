package com.example.P06_FetchingLazyEager.dto;

import java.math.BigDecimal;

public record EmployeeDetailResponse(
        Long id,
        String name,
        String email,
        BigDecimal salary,
        Long departmentId,
        String departmentName,
        String departmentDescription
) {
}
