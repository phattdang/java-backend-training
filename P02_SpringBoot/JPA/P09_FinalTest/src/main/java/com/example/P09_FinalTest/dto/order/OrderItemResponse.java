package com.example.P09_FinalTest.dto.order;

import java.math.BigDecimal;

public record OrderItemResponse(
        Long id,
        Long productId,
        String productCode,
        String productName,
        Integer quantity,
        BigDecimal unitPrice,
        BigDecimal subtotal
) {
}
