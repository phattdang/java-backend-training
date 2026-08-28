package com.example.P09_FinalTest.advanced;

import com.example.P09_FinalTest.entity.Customer;
import com.example.P09_FinalTest.entity.Product;
import com.example.P09_FinalTest.repository.CustomerRepository;
import com.example.P09_FinalTest.repository.OrderRepository;
import com.example.P09_FinalTest.repository.ProductRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
class TransactionRollbackExperimentTests {
    @Autowired
    TransactionExperimentService transactionExperimentService;

    @Autowired
    CustomerRepository customerRepository;

    @Autowired
    ProductRepository productRepository;

    @Autowired
    OrderRepository orderRepository;

    @Autowired
    AdvancedCleanupService advancedCleanupService;

    @AfterEach
    void cleanUp() {
        advancedCleanupService.cleanAdvancedTestData();
    }

    @Test
    void runtimeExceptionRollsBackOrderAndStockByDefault() {
        Seed seed = seed("runtime", 2);

        assertThatThrownBy(() -> transactionExperimentService.createOrderThenFailRuntime(
                seed.customer().getId(),
                seed.product().getId()
        )).isInstanceOf(IllegalStateException.class);

        Product product = productRepository.findById(seed.product().getId()).orElseThrow();
        assertThat(orderRepository.findAll()).isEmpty();
        assertThat(product.getStock()).isEqualTo(2);
    }

    @Test
    void checkedExceptionCommitsByDefault() {
        Seed seed = seed("checked-default", 2);

        assertThatThrownBy(() -> transactionExperimentService.createOrderThenFailCheckedDefault(
                seed.customer().getId(),
                seed.product().getId()
        )).isInstanceOf(Exception.class);

        Product product = productRepository.findById(seed.product().getId()).orElseThrow();
        assertThat(orderRepository.findAll()).hasSize(1);
        assertThat(product.getStock()).isEqualTo(1);
    }

    @Test
    void checkedExceptionRollsBackWhenRollbackForIsDeclared() {
        Seed seed = seed("checked-rollback", 2);

        assertThatThrownBy(() -> transactionExperimentService.createOrderThenFailCheckedRollbackFor(
                seed.customer().getId(),
                seed.product().getId()
        )).isInstanceOf(Exception.class);

        Product product = productRepository.findById(seed.product().getId()).orElseThrow();
        assertThat(orderRepository.findAll()).isEmpty();
        assertThat(product.getStock()).isEqualTo(2);
    }

    private Seed seed(String label, int stock) {
        String suffix = label + "-" + AdvancedTestData.suffix();
        Customer customer = customerRepository.save(AdvancedTestData.customer(suffix));
        Product product = productRepository.save(AdvancedTestData.product(suffix, stock));
        return new Seed(customer, product);
    }

    private record Seed(Customer customer, Product product) {
    }
}
