package com.example.P07_CascadeOrphanRemoval.dto;

import java.util.List;

public record CascadePersistResult(Long parentId, List<Long> childIds, String proof) {
}
