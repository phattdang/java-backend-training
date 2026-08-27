package com.example.P07_CascadeOrphanRemoval.mergedemo.service;

import com.example.P07_CascadeOrphanRemoval.dto.CascadeMergeResult;
import com.example.P07_CascadeOrphanRemoval.dto.CreateOrderRequest;
import com.example.P07_CascadeOrphanRemoval.mergedemo.entity.MergeOrder;
import com.example.P07_CascadeOrphanRemoval.mergedemo.entity.MergeOrderItem;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class MergeDemoService {
    private final EntityManager entityManager;

    @Transactional
    public CascadeMergeResult mergeDetachedGraph(CreateOrderRequest request) {
        MergeOrder seed = new MergeOrder(request.orderCode(), request.customerName());
        request.items().forEach(i -> seed.addItem(
                new MergeOrderItem(i.productName(), i.quantity(), i.price())));

        // MERGE does not include PERSIST, so setup persists every new object explicitly.
        entityManager.persist(seed);
        seed.getItems().forEach(entityManager::persist);
        entityManager.flush();
        Long id = seed.getId();
        entityManager.clear();

        MergeOrder detached = entityManager.find(MergeOrder.class, id);
        detached.getItems().size();
        MergeOrderItem detachedChild = detached.getItems().getFirst();
        entityManager.clear();

        detached.renameCustomer(request.customerName() + " - merged");
        detachedChild.renameProduct(detachedChild.getProductName() + " - merged");
        boolean detachedParentManaged = entityManager.contains(detached);
        boolean detachedChildManaged = entityManager.contains(detachedChild);

        log.info("=== MERGING DETACHED PARENT ===");
        MergeOrder managed = entityManager.merge(detached);
        log.info("=== CASCADE MERGE DETACHED CHILDREN ===");
        entityManager.flush();

        return new CascadeMergeResult(id, detachedParentManaged, detachedChildManaged,
                entityManager.contains(managed), entityManager.contains(managed.getItems().getFirst()),
                detached == managed, managed.getCustomerName(), managed.getItems().getFirst().getProductName());
    }
}
