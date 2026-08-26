package com.example.P02_EntityLifeCycle.dto;

public record PersistenceContextResult(
        Long id,
        boolean firstInstanceManaged,
        boolean secondInstanceManaged,
        boolean sameJavaInstance,
        String observation
) {
}
