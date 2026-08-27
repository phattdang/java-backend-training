package com.example.P05_JPQLCustomQuery.dto;

public record BulkUpdateResult(
        int affectedRows,
        String managedValueBeforeBulkUpdate,
        String databaseValueAfterBulkUpdate,
        String managedValueAfterBulkUpdate,
        boolean managedBeforeClear,
        boolean managedAfterClear,
        String valueAfterClearAndReload
) {
}
