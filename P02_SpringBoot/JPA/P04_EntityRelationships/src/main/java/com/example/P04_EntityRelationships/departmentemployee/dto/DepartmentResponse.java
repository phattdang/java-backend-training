package com.example.P04_EntityRelationships.departmentemployee.dto;

import java.util.List;

public record DepartmentResponse(
        Long id,
        String name,
        List<EmployeeResponse> employees
) {
}
