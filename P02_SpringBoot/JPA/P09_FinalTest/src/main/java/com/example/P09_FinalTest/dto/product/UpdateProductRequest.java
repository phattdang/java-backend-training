package com.example.P09_FinalTest.dto.product;

import com.example.P09_FinalTest.entity.enums.ProductStatus;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;

import java.math.BigDecimal;

public record UpdateProductRequest(
        String name,
        String description,
        @DecimalMin("0.00") BigDecimal price,
        @Min(0) Integer stock,
        ProductStatus status
) {
}
