package com.example.P09_FinalTest.advanced;

import com.example.P09_FinalTest.entity.Product;
import com.example.P09_FinalTest.repository.OrderRepository;
import com.example.P09_FinalTest.repository.ProductRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.RollbackException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
class LockingExperimentTests {
    @Autowired
    ProductRepository productRepository;

    @Autowired
    OrderRepository orderRepository;

    @Autowired
    AdvancedCleanupService advancedCleanupService;

    @Autowired
    EntityManagerFactory entityManagerFactory;

    @AfterEach
    void cleanUp() {
        advancedCleanupService.cleanAdvancedTestData();
    }

    @Test
    void optimisticLockConflictIsThrownWhenTwoTransactionsUpdateSameProductVersion() {
        Product savedProduct = productRepository.save(AdvancedTestData.product("optimistic-" + AdvancedTestData.suffix(), 1));

        EntityManager firstEntityManager = entityManagerFactory.createEntityManager();
        EntityManager secondEntityManager = entityManagerFactory.createEntityManager();
        EntityTransaction firstTransaction = firstEntityManager.getTransaction();
        EntityTransaction secondTransaction = secondEntityManager.getTransaction();

        try {
            firstTransaction.begin();
            secondTransaction.begin();

            Product firstProduct = firstEntityManager.find(Product.class, savedProduct.getId());
            Product secondProduct = secondEntityManager.find(Product.class, savedProduct.getId());

            assertThat(firstProduct.getVersion()).isEqualTo(secondProduct.getVersion());

            firstProduct.setStock(0);
            firstTransaction.commit();

            secondProduct.setStock(0);
            assertThatThrownBy(secondTransaction::commit)
                    .isInstanceOf(RollbackException.class);
        } finally {
            if (firstTransaction.isActive()) {
                firstTransaction.rollback();
            }
            if (secondTransaction.isActive()) {
                secondTransaction.rollback();
            }
            firstEntityManager.close();
            secondEntityManager.close();
        }

        Product reloaded = productRepository.findById(savedProduct.getId()).orElseThrow();
        assertThat(reloaded.getVersion()).isGreaterThan(savedProduct.getVersion());
    }

    @Test
    @Transactional
    void pessimisticWriteRepositoryMethodLoadsProductInsideTransaction() {
        Product product = productRepository.save(AdvancedTestData.product("pessimistic-" + AdvancedTestData.suffix(), 5));

        Product lockedProduct = productRepository.findByIdForUpdate(product.getId()).orElseThrow();

        lockedProduct.setStock(4);
        assertThat(lockedProduct.getStock()).isEqualTo(4);
    }
}
