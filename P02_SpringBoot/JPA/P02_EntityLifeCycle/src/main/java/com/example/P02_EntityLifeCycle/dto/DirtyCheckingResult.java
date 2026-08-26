package com.example.P02_EntityLifeCycle.dto;

public record DirtyCheckingResult(
        Long id,
        String oldName,
        String newName,
        boolean explicitSaveOrMergeCalled,
        String observation
) {
}
