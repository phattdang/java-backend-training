package com.example.P02_EntityLifeCycle.dto;

public record FlushResult(
        Long id,
        String newName,
        boolean flushCalled,
        boolean flushCommitsTransaction,
        String observation
) {
}
