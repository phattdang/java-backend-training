package com.example.P09_FinalTest.controller;

import com.example.P09_FinalTest.dto.order.CreateOrderRequest;
import com.example.P09_FinalTest.dto.order.OrderResponse;
import com.example.P09_FinalTest.dto.order.OrderSummaryResponse;
import com.example.P09_FinalTest.service.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
public class OrderController {
    private final OrderService orderService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public OrderResponse createOrder(@Valid @RequestBody CreateOrderRequest request) {
        return orderService.createOrder(request);
    }

    @GetMapping("/{id}")
    public OrderResponse getOrder(@PathVariable Long id) {
        return orderService.getOrder(id);
    }

    @GetMapping
    public Page<OrderSummaryResponse> getOrders(Pageable pageable) {
        return orderService.getOrders(pageable);
    }

    @DeleteMapping("/{orderId}/items/{itemId}")
    public OrderResponse removeOrderItem(@PathVariable Long orderId, @PathVariable Long itemId) {
        return orderService.removeOrderItem(orderId, itemId);
    }
}
