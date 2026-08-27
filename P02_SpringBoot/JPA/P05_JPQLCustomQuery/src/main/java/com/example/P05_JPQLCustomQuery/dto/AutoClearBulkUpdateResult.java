package com.example.P05_JPQLCustomQuery.dto;

public record AutoClearBulkUpdateResult(
        int affectedRows,
        String valueBeforeBulkUpdate,
        String oldObjectValueAfterBulkUpdate,
        boolean managedBeforeQuery,
        boolean managedAfterQuery,
        String valueAfterReload
) {
}
