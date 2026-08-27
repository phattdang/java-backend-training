package com.example.P07_CascadeOrphanRemoval.mergedemo.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
@Table(name = "merge_order_items")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MergeOrderItem {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String productName;
    @Column(nullable = false)
    private int quantity;
    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal price;
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "order_id", nullable = false)
    private MergeOrder order;

    public MergeOrderItem(String productName, int quantity, BigDecimal price) {
        this.productName = productName;
        this.quantity = quantity;
        this.price = price;
    }

    public void setOrder(MergeOrder order) { this.order = order; }
    public void renameProduct(String productName) { this.productName = productName; }
}
