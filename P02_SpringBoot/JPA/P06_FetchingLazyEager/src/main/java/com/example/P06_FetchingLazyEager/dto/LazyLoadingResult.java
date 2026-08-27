package com.example.P06_FetchingLazyEager.dto;

public record LazyLoadingResult(
        Long employeeId,
        String employeeName,
        boolean initializedBeforeAccess,
        boolean initializedAfterAccess,
        String departmentName
) {
}
