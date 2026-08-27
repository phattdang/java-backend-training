package com.example.P06_FetchingLazyEager.eagerdemo.dto;

public record EagerLoadingResult(
        Long employeeId,
        String employeeName,
        Long departmentId,
        String departmentName,
        boolean departmentInitialized
) {
}
