package com.example.P09_FinalTest.dto.product;

import com.example.P09_FinalTest.entity.enums.ProductStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record ProductResponse(
        Long id,
        String productCode,
        String name,
        String description,
        BigDecimal price,
        Integer stock,
        ProductStatus status,
        Long version,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
}
