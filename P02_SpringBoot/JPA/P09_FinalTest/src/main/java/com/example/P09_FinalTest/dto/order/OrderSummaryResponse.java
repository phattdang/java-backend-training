package com.example.P09_FinalTest.dto.order;

import com.example.P09_FinalTest.entity.enums.OrderStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record OrderSummaryResponse(
        Long orderId,
        String orderCode,
        String customerName,
        OrderStatus status,
        BigDecimal totalAmount,
        LocalDateTime createdAt
) {
}
