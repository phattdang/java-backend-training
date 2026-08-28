package com.example.P09_FinalTest.dto.projection;

import com.example.P09_FinalTest.entity.enums.OrderStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public interface OrderSummaryProjection {
    Long getOrderId();

    String getOrderCode();

    String getCustomerName();

    OrderStatus getStatus();

    BigDecimal getTotalAmount();

    LocalDateTime getCreatedAt();
}
