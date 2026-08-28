package com.example.P08_NPlusOnePractice.dto;

public record EmployeeResponse(
        Long id,
        String name,
        String email,
        Long departmentId,
        String departmentName
) {
}
