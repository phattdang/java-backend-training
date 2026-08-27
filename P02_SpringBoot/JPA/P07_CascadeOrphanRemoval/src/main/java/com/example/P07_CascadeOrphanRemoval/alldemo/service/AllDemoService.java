package com.example.P07_CascadeOrphanRemoval.alldemo.service;

import com.example.P07_CascadeOrphanRemoval.alldemo.entity.AllOrder;
import com.example.P07_CascadeOrphanRemoval.alldemo.entity.AllOrderItem;
import com.example.P07_CascadeOrphanRemoval.dto.CascadePersistResult;
import com.example.P07_CascadeOrphanRemoval.dto.CreateOrderRequest;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class AllDemoService {
    private final EntityManager entityManager;

    @Transactional
    public CascadePersistResult persistOwnedGraph(CreateOrderRequest request) {
        AllOrder order = new AllOrder(request.orderCode(), request.customerName());
        request.items().forEach(i -> order.addItem(
                new AllOrderItem(i.productName(), i.quantity(), i.price())));
        log.info("=== CASCADE ALL: PERSISTING LIFECYCLE-OWNED GRAPH ===");
        entityManager.persist(order);
        entityManager.flush();
        return new CascadePersistResult(order.getId(),
                order.getItems().stream().map(AllOrderItem::getId).toList(),
                "CascadeType.ALL includes PERSIST, MERGE, REMOVE, REFRESH, and DETACH.");
    }
}
