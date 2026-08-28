package com.example.P09_FinalTest.dto.product;

import com.example.P09_FinalTest.entity.enums.ProductStatus;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record CreateProductRequest(
        @NotBlank String productCode,
        @NotBlank String name,
        String description,
        @NotNull @DecimalMin("0.00") BigDecimal price,
        @NotNull @Min(0) Integer stock,
        ProductStatus status
) {
}
