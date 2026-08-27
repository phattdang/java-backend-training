package com.example.P07_CascadeOrphanRemoval.dto;

public record CascadeMergeResult(
        Long parentId,
        boolean detachedParentManagedBeforeMerge,
        boolean detachedChildManagedBeforeMerge,
        boolean returnedParentManaged,
        boolean returnedChildManaged,
        boolean mergeReturnedSameInstance,
        String mergedCustomerName,
        String mergedProductName
) {
}
