package com.example.P06_FetchingLazyEager.dto;

public record ProxyInspectionResult(
        String runtimeClass,
        boolean initializedBeforeAccess,
        boolean initializedAfterAccess,
        String departmentName
) {
}
