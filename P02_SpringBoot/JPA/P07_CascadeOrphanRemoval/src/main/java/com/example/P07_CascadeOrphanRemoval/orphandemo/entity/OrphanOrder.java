package com.example.P07_CascadeOrphanRemoval.orphandemo.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "orphan_orders")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OrphanOrder {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, unique = true)
    private String orderCode;
    @Column(nullable = false)
    private String customerName;
    @OneToMany(mappedBy = "order", orphanRemoval = true)
    private List<OrphanOrderItem> items = new ArrayList<>();

    public OrphanOrder(String orderCode, String customerName) {
        this.orderCode = orderCode;
        this.customerName = customerName;
    }

    // These helpers synchronize both Java references; orphanRemoval acts at flush time.
    public void addItem(OrphanOrderItem item) {
        items.add(item);
        item.setOrder(this);
    }

    public void removeItem(OrphanOrderItem item) {
        items.remove(item);
        item.setOrder(null);
    }
}
