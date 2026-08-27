package com.example.P07_CascadeOrphanRemoval.nocascadedemo.service;

import com.example.P07_CascadeOrphanRemoval.dto.CascadePersistResult;
import com.example.P07_CascadeOrphanRemoval.dto.CreateOrderRequest;
import com.example.P07_CascadeOrphanRemoval.dto.NoCascadeRemoveResult;
import com.example.P07_CascadeOrphanRemoval.nocascadedemo.entity.NoCascadeOrder;
import com.example.P07_CascadeOrphanRemoval.nocascadedemo.entity.NoCascadeOrderItem;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionTemplate;

@Service
@RequiredArgsConstructor
@Slf4j
public class NoCascadeDemoService {
    private final EntityManager entityManager;
    private final TransactionTemplate transactionTemplate;

    @Transactional
    public CascadePersistResult create(CreateOrderRequest request) {
        NoCascadeOrder order = new NoCascadeOrder(request.orderCode(), request.customerName());
        request.items().forEach(i -> order.addItem(
                new NoCascadeOrderItem(i.productName(), i.quantity(), i.price())));
        entityManager.persist(order);
        order.getItems().forEach(entityManager::persist);
        entityManager.flush();
        return new CascadePersistResult(order.getId(),
                order.getItems().stream().map(NoCascadeOrderItem::getId).toList(),
                "No cascade is configured, so every child was explicitly persisted.");
    }

    public NoCascadeRemoveResult attemptParentDelete(Long parentId) {
        int childrenBefore = transactionTemplate.execute(status -> entityManager.createQuery(
                        "select count(i) from NoCascadeOrderItem i where i.order.id = :id", Long.class)
                .setParameter("id", parentId).getSingleResult().intValue());
        boolean blocked = false;
        try {
            transactionTemplate.executeWithoutResult(status -> {
                NoCascadeOrder order = entityManager.find(NoCascadeOrder.class, parentId);
                if (order == null) throw new IllegalArgumentException("NoCascadeOrder not found: " + parentId);
                log.info("=== ATTEMPTING PARENT DELETE WITHOUT CASCADE REMOVE ===");
                entityManager.remove(order);
                entityManager.flush();
            });
        } catch (RuntimeException exception) {
            blocked = true;
            log.info("=== FOREIGN KEY BLOCKED PARENT DELETE AS EXPECTED ===");
        }
        boolean parentExists = Boolean.TRUE.equals(transactionTemplate.execute(status ->
                entityManager.find(NoCascadeOrder.class, parentId) != null));
        return new NoCascadeRemoveResult(parentId, childrenBefore, blocked, parentExists,
                blocked
                        ? "The child FK still references the parent, and no REMOVE operation propagated."
                        : "Delete was not blocked; inspect the actual database FK/schema configuration.");
    }
}
