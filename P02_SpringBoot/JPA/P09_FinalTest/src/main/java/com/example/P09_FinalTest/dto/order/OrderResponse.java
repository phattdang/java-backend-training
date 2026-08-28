package com.example.P09_FinalTest.dto.order;

import com.example.P09_FinalTest.entity.enums.OrderStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public record OrderResponse(
        Long id,
        String orderCode,
        Long customerId,
        String customerName,
        OrderStatus status,
        BigDecimal totalAmount,
        List<OrderItemResponse> items,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
}
