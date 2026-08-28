package com.example.P08_NPlusOnePractice.dto;

public record DepartmentEmployeeView(
        Long departmentId,
        String departmentName,
        Long employeeId,
        String employeeName
) {
}
