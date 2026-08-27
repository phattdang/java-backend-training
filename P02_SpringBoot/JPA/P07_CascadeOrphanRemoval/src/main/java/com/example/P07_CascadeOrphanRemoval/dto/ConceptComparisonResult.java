package com.example.P07_CascadeOrphanRemoval.dto;

public record ConceptComparisonResult(
        String cascadeRemoveTrigger,
        String cascadeRemoveResult,
        String orphanRemovalTrigger,
        String orphanRemovalResult,
        String memoryRule
) {
}
