package com.example.P07_CascadeOrphanRemoval.dto;

public record CascadeRemoveResult(
        Long parentId,
        int childrenBeforeDelete,
        boolean parentExistsAfter,
        long childrenRemainingAfter
) {
}
