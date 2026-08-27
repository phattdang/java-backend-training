package com.example.P07_CascadeOrphanRemoval.orphandemo.service;

import com.example.P07_CascadeOrphanRemoval.dto.CascadePersistResult;
import com.example.P07_CascadeOrphanRemoval.dto.CreateOrderRequest;
import com.example.P07_CascadeOrphanRemoval.dto.OrphanRemovalResult;
import com.example.P07_CascadeOrphanRemoval.orphandemo.entity.OrphanOrder;
import com.example.P07_CascadeOrphanRemoval.orphandemo.entity.OrphanOrderItem;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrphanDemoService {
    private final EntityManager entityManager;

    @Transactional
    public CascadePersistResult create(CreateOrderRequest request) {
        OrphanOrder order = new OrphanOrder(request.orderCode(), request.customerName());
        request.items().forEach(i -> order.addItem(
                new OrphanOrderItem(i.productName(), i.quantity(), i.price())));
        entityManager.persist(order);
        order.getItems().forEach(entityManager::persist);
        entityManager.flush();
        return new CascadePersistResult(order.getId(),
                order.getItems().stream().map(OrphanOrderItem::getId).toList(),
                "Setup persists explicitly; orphanRemoval is observed only when unlinking a child.");
    }

    @Transactional
    public OrphanRemovalResult removeOneChild(Long parentId, Long childId) {
        OrphanOrder order = entityManager.find(OrphanOrder.class, parentId);
        if (order == null) throw new IllegalArgumentException("OrphanOrder not found: " + parentId);
        OrphanOrderItem child = order.getItems().stream()
                .filter(i -> i.getId().equals(childId)).findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Child is not owned by this order: " + childId));
        log.info("=== REMOVING ONE CHILD FROM COLLECTION ===");
        order.removeItem(child);
        log.info("=== ORPHAN REMOVAL SHOULD DELETE ONLY THAT CHILD ===");
        entityManager.flush();
        entityManager.clear();
        OrphanOrder reloaded = entityManager.find(OrphanOrder.class, parentId);
        return new OrphanRemovalResult(parentId, childId, reloaded != null,
                entityManager.find(OrphanOrderItem.class, childId) != null, reloaded.getItems().size());
    }
}
