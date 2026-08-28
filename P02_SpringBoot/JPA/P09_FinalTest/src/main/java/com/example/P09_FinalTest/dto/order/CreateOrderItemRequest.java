package com.example.P09_FinalTest.dto.order;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record CreateOrderItemRequest(
        @NotNull Long productId,
        @NotNull @Positive Integer quantity
) {
}
