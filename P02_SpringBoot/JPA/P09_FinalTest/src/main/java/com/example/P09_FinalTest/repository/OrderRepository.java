package com.example.P09_FinalTest.repository;

import com.example.P09_FinalTest.dto.projection.OrderSummaryProjection;
import com.example.P09_FinalTest.entity.Order;
import com.example.P09_FinalTest.entity.enums.OrderStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order, Long> {
    boolean existsByOrderCode(String orderCode);

    List<Order> findByCustomerId(Long customerId);

    Page<Order> findByStatus(OrderStatus status, Pageable pageable);

    @Query("""
            select distinct o
            from CustomerOrder o
            join fetch o.customer
            left join fetch o.orderItems oi
            left join fetch oi.product
            where o.id = :id
            """)
    Optional<Order> findDetailByIdUsingJoinFetch(@Param("id") Long id);

    @EntityGraph(attributePaths = {"customer", "orderItems", "orderItems.product"})
    @Query("select o from CustomerOrder o where o.id = :id")
    Optional<Order> findDetailByIdUsingEntityGraph(@Param("id") Long id);

    @Query("""
            select
                o.id as orderId,
                o.orderCode as orderCode,
                c.fullName as customerName,
                o.status as status,
                o.totalAmount as totalAmount,
                o.createdAt as createdAt
            from CustomerOrder o
            join o.customer c
            """)
    Page<OrderSummaryProjection> findOrderSummaries(Pageable pageable);
}
