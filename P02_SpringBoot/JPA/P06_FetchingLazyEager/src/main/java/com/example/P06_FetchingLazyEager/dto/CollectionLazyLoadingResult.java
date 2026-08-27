package com.example.P06_FetchingLazyEager.dto;

import java.util.List;

public record CollectionLazyLoadingResult(
        Long departmentId,
        String departmentName,
        boolean initializedBeforeAccess,
        boolean initializedAfterAccess,
        int employeeCount,
        List<String> employeeNames
) {
}
