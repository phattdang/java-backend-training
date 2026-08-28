package com.example.P08_NPlusOnePractice.dto;

import java.util.List;

public record DepartmentResponse(
        Long id,
        String name,
        List<EmployeeSummary> employees
) {
}
