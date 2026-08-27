package com.example.P07_CascadeOrphanRemoval.dto;

public record NoCascadeRemoveResult(
        Long parentId,
        int childrenBeforeDelete,
        boolean deletionBlockedByForeignKey,
        boolean parentStillExists,
        String explanation
) {
}
