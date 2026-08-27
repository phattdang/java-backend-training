package com.example.P07_CascadeOrphanRemoval.nocascadedemo.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "no_cascade_orders")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class NoCascadeOrder {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, unique = true)
    private String orderCode;
    @Column(nullable = false)
    private String customerName;
    @OneToMany(mappedBy = "order")
    private List<NoCascadeOrderItem> items = new ArrayList<>();

    public NoCascadeOrder(String orderCode, String customerName) {
        this.orderCode = orderCode;
        this.customerName = customerName;
    }

    public void addItem(NoCascadeOrderItem item) {
        items.add(item);
        item.setOrder(this);
    }
}
