package com.example.P07_CascadeOrphanRemoval.persistdemo.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "persist_orders")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PersistOrder {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, unique = true)
    private String orderCode;
    @Column(nullable = false)
    private String customerName;
    @OneToMany(mappedBy = "order", cascade = CascadeType.PERSIST)
    private List<PersistOrderItem> items = new ArrayList<>();

    public PersistOrder(String orderCode, String customerName) {
        this.orderCode = orderCode;
        this.customerName = customerName;
    }

    // Synchronizes the Java object graph; this method does not persist anything itself.
    public void addItem(PersistOrderItem item) {
        items.add(item);
        item.setOrder(this);
    }
}
