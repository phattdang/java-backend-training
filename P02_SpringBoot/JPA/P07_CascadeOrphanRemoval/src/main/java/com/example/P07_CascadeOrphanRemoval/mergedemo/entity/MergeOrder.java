package com.example.P07_CascadeOrphanRemoval.mergedemo.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "merge_orders")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MergeOrder {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, unique = true)
    private String orderCode;
    @Column(nullable = false)
    private String customerName;
    @OneToMany(mappedBy = "order", cascade = CascadeType.MERGE)
    private List<MergeOrderItem> items = new ArrayList<>();

    public MergeOrder(String orderCode, String customerName) {
        this.orderCode = orderCode;
        this.customerName = customerName;
    }

    public void addItem(MergeOrderItem item) {
        items.add(item);
        item.setOrder(this);
    }

    public void renameCustomer(String customerName) {
        this.customerName = customerName;
    }
}
