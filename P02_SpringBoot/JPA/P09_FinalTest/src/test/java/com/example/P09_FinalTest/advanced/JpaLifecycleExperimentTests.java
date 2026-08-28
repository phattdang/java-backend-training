package com.example.P09_FinalTest.advanced;

import com.example.P09_FinalTest.entity.Product;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
class JpaLifecycleExperimentTests {
    @Autowired
    EntityManager entityManager;

    @Test
    void persistenceContextDirtyCheckingDetachMergeAndRemoveCanBeObserved() {
        String suffix = AdvancedTestData.suffix();
        Product transientProduct = AdvancedTestData.product(suffix, 5);

        assertThat(entityManager.contains(transientProduct)).isFalse();

        entityManager.persist(transientProduct);
        assertThat(entityManager.contains(transientProduct)).isTrue();

        entityManager.flush();
        Long productId = transientProduct.getId();
        assertThat(productId).isNotNull();

        Product firstFind = entityManager.find(Product.class, productId);
        Product secondFind = entityManager.find(Product.class, productId);
        assertThat(firstFind).isSameAs(secondFind);

        firstFind.setPrice(new BigDecimal("120.00"));
        entityManager.flush();
        entityManager.clear();

        Product reloaded = entityManager.find(Product.class, productId);
        assertThat(reloaded.getPrice()).isEqualByComparingTo("120.00");

        entityManager.detach(reloaded);
        assertThat(entityManager.contains(reloaded)).isFalse();

        reloaded.setName("Detached Product " + suffix);
        Product merged = entityManager.merge(reloaded);
        assertThat(entityManager.contains(merged)).isTrue();

        entityManager.remove(merged);
        assertThat(entityManager.contains(merged)).isFalse();

        entityManager.flush();
        entityManager.clear();
        assertThat(entityManager.find(Product.class, productId)).isNull();
    }
}
