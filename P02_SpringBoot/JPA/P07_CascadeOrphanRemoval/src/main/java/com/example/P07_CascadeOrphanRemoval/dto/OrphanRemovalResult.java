package com.example.P07_CascadeOrphanRemoval.dto;

public record OrphanRemovalResult(
        Long parentId,
        Long removedChildId,
        boolean parentExistsAfter,
        boolean removedChildExistsAfter,
        int remainingChildren
) {
}
