package com.example.P04_EntityRelationships.departmentemployee.dto;

public record EmployeeResponse(
        Long id,
        String name,
        Long departmentId
) {
}
