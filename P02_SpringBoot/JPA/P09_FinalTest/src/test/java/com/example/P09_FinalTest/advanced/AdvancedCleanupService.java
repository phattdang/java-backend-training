package com.example.P09_FinalTest.advanced;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AdvancedCleanupService {
    private final EntityManager entityManager;

    @Transactional
    public void cleanAdvancedTestData() {
        entityManager.createNativeQuery("""
                delete from order_items
                where order_id in (
                    select id from orders
                    where order_code like 'ADVORD-%' or order_code like 'ADVEXP-%'
                )
                """).executeUpdate();
        entityManager.createNativeQuery("""
                delete from orders
                where order_code like 'ADVORD-%' or order_code like 'ADVEXP-%'
                """).executeUpdate();
        entityManager.createNativeQuery("""
                delete from products
                where product_code like 'ADV-%'
                """).executeUpdate();
        entityManager.createNativeQuery("""
                delete from customers
                where email like 'advanced-%@example.com'
                """).executeUpdate();
    }
}
