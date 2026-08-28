package com.example.P09_FinalTest.service;

import com.example.P09_FinalTest.dto.order.CreateOrderItemRequest;
import com.example.P09_FinalTest.dto.order.CreateOrderRequest;
import com.example.P09_FinalTest.dto.order.OrderItemResponse;
import com.example.P09_FinalTest.dto.order.OrderResponse;
import com.example.P09_FinalTest.dto.order.OrderSummaryResponse;
import com.example.P09_FinalTest.dto.projection.OrderSummaryProjection;
import com.example.P09_FinalTest.entity.Customer;
import com.example.P09_FinalTest.entity.Order;
import com.example.P09_FinalTest.entity.OrderItem;
import com.example.P09_FinalTest.entity.Product;
import com.example.P09_FinalTest.entity.enums.OrderStatus;
import com.example.P09_FinalTest.entity.enums.ProductStatus;
import com.example.P09_FinalTest.repository.CustomerRepository;
import com.example.P09_FinalTest.repository.OrderRepository;
import com.example.P09_FinalTest.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final CustomerRepository customerRepository;
    private final ProductRepository productRepository;
    private final OrderRepository orderRepository;

    @Transactional
    public OrderResponse createOrder(CreateOrderRequest request) {
        Customer customer = customerRepository.findById(request.customerId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Customer not found"));

        Order order = new Order();
        order.setOrderCode(generateOrderCode());
        order.setCustomer(customer);
        order.setStatus(OrderStatus.PENDING);
        order.setTotalAmount(BigDecimal.ZERO);

        BigDecimal totalAmount = BigDecimal.ZERO;
        for (CreateOrderItemRequest itemRequest : request.items()) {
            Product product = productRepository.findById(itemRequest.productId())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Product not found"));

            if (product.getStatus() != ProductStatus.ACTIVE) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Product is not active");
            }
            if (product.getStock() < itemRequest.quantity()) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Not enough stock");
            }

            BigDecimal subtotal = product.getPrice().multiply(BigDecimal.valueOf(itemRequest.quantity()));

            OrderItem orderItem = new OrderItem();
            orderItem.setProduct(product);
            orderItem.setQuantity(itemRequest.quantity());
            orderItem.setUnitPrice(product.getPrice());
            orderItem.setSubtotal(subtotal);

            product.setStock(product.getStock() - itemRequest.quantity());
            if (product.getStock() == 0) {
                product.setStatus(ProductStatus.OUT_OF_STOCK);
            }

            order.addItem(orderItem);
            totalAmount = totalAmount.add(subtotal);
        }

        order.setTotalAmount(totalAmount);
        Order savedOrder = orderRepository.save(order);

        return toResponse(savedOrder);
    }

    @Transactional(readOnly = true)
    public OrderResponse getOrder(Long id) {
        Order order = orderRepository.findDetailByIdUsingJoinFetch(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Order not found"));

        return toResponse(order);
    }

    @Transactional(readOnly = true)
    public Page<OrderSummaryResponse> getOrders(Pageable pageable) {
        return orderRepository.findOrderSummaries(pageable).map(this::toSummaryResponse);
    }

    @Transactional
    public OrderResponse removeOrderItem(Long orderId, Long itemId) {
        Order order = orderRepository.findDetailByIdUsingJoinFetch(orderId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Order not found"));

        OrderItem item = order.getOrderItems().stream()
                .filter(orderItem -> orderItem.getId().equals(itemId))
                .findFirst()
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Order item not found"));

        Product product = item.getProduct();
        product.setStock(product.getStock() + item.getQuantity());
        if (product.getStatus() == ProductStatus.OUT_OF_STOCK && product.getStock() > 0) {
            product.setStatus(ProductStatus.ACTIVE);
        }

        order.setTotalAmount(order.getTotalAmount().subtract(item.getSubtotal()));
        order.removeItem(item);

        return toResponse(order);
    }

    private String generateOrderCode() {
        String orderCode;
        do {
            orderCode = "ORD-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        } while (orderRepository.existsByOrderCode(orderCode));
        return orderCode;
    }

    private OrderResponse toResponse(Order order) {
        return new OrderResponse(
                order.getId(),
                order.getOrderCode(),
                order.getCustomer().getId(),
                order.getCustomer().getFullName(),
                order.getStatus(),
                order.getTotalAmount(),
                order.getOrderItems().stream().map(this::toItemResponse).toList(),
                order.getCreatedAt(),
                order.getUpdatedAt()
        );
    }

    private OrderItemResponse toItemResponse(OrderItem item) {
        Product product = item.getProduct();
        return new OrderItemResponse(
                item.getId(),
                product.getId(),
                product.getProductCode(),
                product.getName(),
                item.getQuantity(),
                item.getUnitPrice(),
                item.getSubtotal()
        );
    }

    private OrderSummaryResponse toSummaryResponse(OrderSummaryProjection projection) {
        return new OrderSummaryResponse(
                projection.getOrderId(),
                projection.getOrderCode(),
                projection.getCustomerName(),
                projection.getStatus(),
                projection.getTotalAmount(),
                projection.getCreatedAt()
        );
    }
}
