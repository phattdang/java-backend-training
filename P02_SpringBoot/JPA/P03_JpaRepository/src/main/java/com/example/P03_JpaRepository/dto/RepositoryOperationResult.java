package com.example.P03_JpaRepository.dto;

public record RepositoryOperationResult(
        String operation,
        String explanation,
        Object data
) {
}
