package com.example.P04_EntityRelationships.studentcourse.dto;

import java.util.Set;

public record StudentResponse(
        Long id,
        String name,
        Set<Long> courseIds
) {
}
