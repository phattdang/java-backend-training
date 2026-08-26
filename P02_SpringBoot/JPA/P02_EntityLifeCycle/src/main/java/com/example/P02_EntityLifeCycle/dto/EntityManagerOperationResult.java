package com.example.P02_EntityLifeCycle.dto;

public record EntityManagerOperationResult(
        Long id,
        String operation,
        boolean originalInstanceManaged,
        boolean returnedInstanceManaged,
        boolean sameJavaInstance,
        String observation
) {
}
