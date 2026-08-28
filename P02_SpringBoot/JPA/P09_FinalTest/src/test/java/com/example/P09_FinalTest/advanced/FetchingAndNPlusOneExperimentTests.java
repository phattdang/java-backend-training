package com.example.P09_FinalTest.advanced;

import com.example.P09_FinalTest.entity.Customer;
import com.example.P09_FinalTest.entity.Order;
import com.example.P09_FinalTest.entity.OrderItem;
import com.example.P09_FinalTest.entity.Product;
import com.example.P09_FinalTest.entity.enums.OrderStatus;
import com.example.P09_FinalTest.repository.CustomerRepository;
import com.example.P09_FinalTest.repository.OrderRepository;
import com.example.P09_FinalTest.repository.ProductRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import org.hibernate.LazyInitializationException;
import org.hibernate.SessionFactory;
import org.hibernate.stat.Statistics;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
class FetchingAndNPlusOneExperimentTests {
    @Autowired
    CustomerRepository customerRepository;

    @Autowired
    ProductRepository productRepository;

    @Autowired
    OrderRepository orderRepository;

    @Autowired
    EntityManager entityManager;

    @Autowired
    EntityManagerFactory entityManagerFactory;

    @Autowired
    AdvancedCleanupService advancedCleanupService;

    @AfterEach
    void cleanUp() {
        advancedCleanupService.cleanAdvancedTestData();
    }

    @Test
    void accessingLazyCollectionOutsidePersistenceContextThrowsLazyInitializationException() {
        Order order = seedOrder("lazy");

        Order loadedOrder = orderRepository.findById(order.getId()).orElseThrow();

        assertThatThrownBy(() -> loadedOrder.getOrderItems().size())
                .isInstanceOf(LazyInitializationException.class);
    }

    @Test
    @Transactional
    void loadingOrdersThenAccessingItemsAndProductsCreatesNPlusOneQueries() {
        seedOrder("nplusone-a");
        seedOrder("nplusone-b");
        entityManager.flush();
        entityManager.clear();

        Statistics statistics = statistics();
        statistics.clear();

        orderRepository.findAll().forEach(order -> order.getOrderItems().forEach(item -> {
            item.getProduct().getName();
        }));

        assertThat(statistics.getPrepareStatementCount()).isGreaterThan(1);
    }

    @Test
    @Transactional
    void joinFetchLoadsOrderDetailWithoutLazyFollowUpQueries() {
        Order order = seedOrder("join-fetch");
        entityManager.flush();
        entityManager.clear();

        Statistics statistics = statistics();
        statistics.clear();

        Order loadedOrder = orderRepository.findDetailByIdUsingJoinFetch(order.getId()).orElseThrow();
        loadedOrder.getOrderItems().forEach(item -> item.getProduct().getName());

        assertThat(statistics.getPrepareStatementCount()).isEqualTo(1);
    }

    @Test
    @Transactional
    void entityGraphLoadsOrderDetailWithoutLazyFollowUpQueries() {
        Order order = seedOrder("entity-graph");
        entityManager.flush();
        entityManager.clear();

        Statistics statistics = statistics();
        statistics.clear();

        Order loadedOrder = orderRepository.findDetailByIdUsingEntityGraph(order.getId()).orElseThrow();
        loadedOrder.getOrderItems().forEach(item -> item.getProduct().getName());

        assertThat(statistics.getPrepareStatementCount()).isEqualTo(1);
    }

    private Statistics statistics() {
        return entityManagerFactory.unwrap(SessionFactory.class).getStatistics();
    }

    private Order seedOrder(String label) {
        String suffix = label + "-" + AdvancedTestData.suffix();
        Customer customer = customerRepository.save(AdvancedTestData.customer(suffix));
        Product product = productRepository.save(AdvancedTestData.product(suffix, 10));

        Order order = new Order();
        order.setOrderCode("ADVORD-" + suffix);
        order.setCustomer(customer);
        order.setStatus(OrderStatus.PENDING);
        order.setTotalAmount(new BigDecimal("100.00"));

        OrderItem item = new OrderItem();
        item.setProduct(product);
        item.setQuantity(1);
        item.setUnitPrice(product.getPrice());
        item.setSubtotal(product.getPrice());
        order.addItem(item);

        return orderRepository.save(order);
    }
}
