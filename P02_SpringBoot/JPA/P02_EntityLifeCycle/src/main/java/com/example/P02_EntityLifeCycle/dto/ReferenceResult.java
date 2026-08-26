package com.example.P02_EntityLifeCycle.dto;

public record ReferenceResult(
        Long requestedId,
        Long referenceId,
        String runtimeClass,
        boolean managed,
        String observation
) {
}
