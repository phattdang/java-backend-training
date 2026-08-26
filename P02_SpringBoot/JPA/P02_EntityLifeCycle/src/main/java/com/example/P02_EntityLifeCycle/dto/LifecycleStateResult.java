package com.example.P02_EntityLifeCycle.dto;

public record LifecycleStateResult(
        Long id,
        String lifecycleState,
        boolean containedBeforeOperation,
        boolean containedAfterOperation,
        String observation
) {
}
