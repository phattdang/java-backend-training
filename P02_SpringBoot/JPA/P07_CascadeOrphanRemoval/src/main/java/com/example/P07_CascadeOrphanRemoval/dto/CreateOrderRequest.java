package com.example.P07_CascadeOrphanRemoval.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;

import java.util.List;

public record CreateOrderRequest(
        @NotBlank String orderCode,
        @NotBlank String customerName,
        @NotEmpty List<@Valid ItemRequest> items
) {
}
