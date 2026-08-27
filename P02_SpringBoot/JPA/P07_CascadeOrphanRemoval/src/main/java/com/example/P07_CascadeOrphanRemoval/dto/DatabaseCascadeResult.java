package com.example.P07_CascadeOrphanRemoval.dto;

public record DatabaseCascadeResult(
        Long parentId,
        int childrenBeforeDirectSqlDelete,
        int parentRowsDeleted,
        int childrenAfterDirectSqlDelete,
        String mechanism
) {
}
