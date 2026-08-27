package com.example.P07_CascadeOrphanRemoval.persistdemo.service;

import com.example.P07_CascadeOrphanRemoval.dto.CascadePersistResult;
import com.example.P07_CascadeOrphanRemoval.dto.CreateOrderRequest;
import com.example.P07_CascadeOrphanRemoval.persistdemo.entity.PersistOrder;
import com.example.P07_CascadeOrphanRemoval.persistdemo.entity.PersistOrderItem;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class PersistDemoService {
    private final EntityManager entityManager;

    @Transactional
    public CascadePersistResult persistParentOnly(CreateOrderRequest request) {
        PersistOrder order = new PersistOrder(request.orderCode(), request.customerName());
        request.items().forEach(i -> order.addItem(
                new PersistOrderItem(i.productName(), i.quantity(), i.price())));

        log.info("=== PERSISTING PARENT ONLY ===");
        entityManager.persist(order);
        log.info("=== CASCADE PERSIST CHILDREN ===");
        entityManager.flush();

        return new CascadePersistResult(order.getId(),
                order.getItems().stream().map(PersistOrderItem::getId).toList(),
                "Only entityManager.persist(order) was called; child INSERTs came from CascadeType.PERSIST.");
    }
}
