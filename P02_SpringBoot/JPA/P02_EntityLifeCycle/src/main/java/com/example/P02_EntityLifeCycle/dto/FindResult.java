package com.example.P02_EntityLifeCycle.dto;

public record FindResult(
        Long requestedId,
        boolean found,
        String name,
        boolean managed,
        String observation
) {
}
