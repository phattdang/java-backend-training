package com.example.P07_CascadeOrphanRemoval.removedemo.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "remove_orders")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RemoveOrder {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, unique = true)
    private String orderCode;
    @Column(nullable = false)
    private String customerName;
    @OneToMany(mappedBy = "order", cascade = CascadeType.REMOVE)
    private List<RemoveOrderItem> items = new ArrayList<>();

    public RemoveOrder(String orderCode, String customerName) {
        this.orderCode = orderCode;
        this.customerName = customerName;
    }

    public void addItem(RemoveOrderItem item) {
        items.add(item);
        item.setOrder(this);
    }
}
