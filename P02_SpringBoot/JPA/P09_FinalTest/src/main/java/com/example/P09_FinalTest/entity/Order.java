package com.example.P09_FinalTest.entity;

import com.example.P09_FinalTest.entity.enums.OrderStatus;
import jakarta.validation.constraints.DecimalMin;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity(name = "CustomerOrder")
@Table(name = "orders")
@EntityListeners(AuditingEntityListener.class)
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(name = "order_code", nullable = false, unique = true)
    String orderCode;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id", nullable = false)
    Customer customer;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    OrderStatus status;

    @DecimalMin("0.00")
    @Column(name = "total_amount", nullable = false, precision = 19, scale = 2)
    BigDecimal totalAmount;

    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updated_at")
    LocalDateTime updatedAt;

    @OneToMany(
            mappedBy = "order",
            fetch = FetchType.LAZY,
            cascade = {CascadeType.PERSIST, CascadeType.REMOVE},
            orphanRemoval = true
    )
    List<OrderItem> orderItems = new ArrayList<>();

    public void addItem(OrderItem item) {
        orderItems.add(item);
        item.setOrder(this);
    }

    public void removeItem(OrderItem item) {
        orderItems.remove(item);
        item.setOrder(null);
    }
}
