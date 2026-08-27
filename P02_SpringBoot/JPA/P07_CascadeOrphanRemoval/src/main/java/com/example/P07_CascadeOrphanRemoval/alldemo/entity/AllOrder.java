package com.example.P07_CascadeOrphanRemoval.alldemo.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "all_orders")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AllOrder {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, unique = true)
    private String orderCode;
    @Column(nullable = false)
    private String customerName;
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<AllOrderItem> items = new ArrayList<>();

    public AllOrder(String orderCode, String customerName) {
        this.orderCode = orderCode;
        this.customerName = customerName;
    }

    public void addItem(AllOrderItem item) {
        items.add(item);
        item.setOrder(this);
    }
}
