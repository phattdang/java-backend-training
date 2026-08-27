package com.example.P06_FetchingLazyEager.dto;

public record RelationshipInitializationResult(
        Long employeeId,
        String employeeName,
        boolean departmentInitialized
) {
}
