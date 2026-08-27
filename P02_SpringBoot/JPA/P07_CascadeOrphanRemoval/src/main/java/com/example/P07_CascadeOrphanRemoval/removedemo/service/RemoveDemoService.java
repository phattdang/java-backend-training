package com.example.P07_CascadeOrphanRemoval.removedemo.service;

import com.example.P07_CascadeOrphanRemoval.dto.CascadePersistResult;
import com.example.P07_CascadeOrphanRemoval.dto.CascadeRemoveResult;
import com.example.P07_CascadeOrphanRemoval.dto.CreateOrderRequest;
import com.example.P07_CascadeOrphanRemoval.removedemo.entity.RemoveOrder;
import com.example.P07_CascadeOrphanRemoval.removedemo.entity.RemoveOrderItem;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class RemoveDemoService {
    private final EntityManager entityManager;

    @Transactional
    public CascadePersistResult create(CreateOrderRequest request) {
        RemoveOrder order = new RemoveOrder(request.orderCode(), request.customerName());
        request.items().forEach(i -> order.addItem(
                new RemoveOrderItem(i.productName(), i.quantity(), i.price())));
        entityManager.persist(order);
        order.getItems().forEach(entityManager::persist);
        entityManager.flush();
        return new CascadePersistResult(order.getId(),
                order.getItems().stream().map(RemoveOrderItem::getId).toList(),
                "Setup persists children explicitly because this mapping has REMOVE only.");
    }

    @Transactional
    public CascadeRemoveResult removeParent(Long parentId) {
        RemoveOrder order = entityManager.find(RemoveOrder.class, parentId);
        if (order == null) throw new IllegalArgumentException("RemoveOrder not found: " + parentId);
        int before = order.getItems().size();
        log.info("=== REMOVING PARENT ===");
        entityManager.remove(order);
        log.info("=== CHILDREN SHOULD BE REMOVED BY CASCADE ===");
        entityManager.flush();
        entityManager.clear();
        long remaining = entityManager.createQuery(
                        "select count(i) from RemoveOrderItem i where i.order.id = :id", Long.class)
                .setParameter("id", parentId).getSingleResult();
        return new CascadeRemoveResult(parentId, before,
                entityManager.find(RemoveOrder.class, parentId) != null, remaining);
    }
}
