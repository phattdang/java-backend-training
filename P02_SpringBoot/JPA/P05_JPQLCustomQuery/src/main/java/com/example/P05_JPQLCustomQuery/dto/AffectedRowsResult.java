package com.example.P05_JPQLCustomQuery.dto;

public record AffectedRowsResult(
        String operation,
        int affectedRows
) {
}
