package com.example.P05_JPQLCustomQuery.dto;

import com.example.P05_JPQLCustomQuery.entity.Employee;

import java.math.BigDecimal;

public record EmployeeResponse(
        Long id,
        String name,
        String email,
        BigDecimal salary,
        boolean active,
        Long departmentId,
        String departmentName
) {
    public static EmployeeResponse from(Employee employee) {
        return new EmployeeResponse(
                employee.getId(),
                employee.getName(),
                employee.getEmail(),
                employee.getSalary(),
                employee.isActive(),
                employee.getDepartment().getId(),
                employee.getDepartment().getName()
        );
    }
}
