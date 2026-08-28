package com.example.P09_FinalTest.advanced;

import com.example.P09_FinalTest.entity.Customer;
import com.example.P09_FinalTest.entity.Order;
import com.example.P09_FinalTest.entity.OrderItem;
import com.example.P09_FinalTest.entity.Product;
import com.example.P09_FinalTest.entity.enums.OrderStatus;
import com.example.P09_FinalTest.repository.CustomerRepository;
import com.example.P09_FinalTest.repository.OrderRepository;
import com.example.P09_FinalTest.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TransactionExperimentService {
    private final CustomerRepository customerRepository;
    private final ProductRepository productRepository;
    private final OrderRepository orderRepository;

    @Transactional
    public void createOrderThenFailRuntime(Long customerId, Long productId) {
        createOrderAndDecreaseStock(customerId, productId);
        throw new IllegalStateException("Runtime exception should rollback by default");
    }

    @Transactional
    public void createOrderThenFailCheckedDefault(Long customerId, Long productId) throws Exception {
        createOrderAndDecreaseStock(customerId, productId);
        throw new Exception("Checked exception does not rollback by default");
    }

    @Transactional(rollbackFor = Exception.class)
    public void createOrderThenFailCheckedRollbackFor(Long customerId, Long productId) throws Exception {
        createOrderAndDecreaseStock(customerId, productId);
        throw new Exception("Checked exception should rollback with rollbackFor");
    }

    private void createOrderAndDecreaseStock(Long customerId, Long productId) {
        Customer customer = customerRepository.findById(customerId).orElseThrow();
        Product product = productRepository.findById(productId).orElseThrow();

        Order order = new Order();
        order.setOrderCode("ADVEXP-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase());
        order.setCustomer(customer);
        order.setStatus(OrderStatus.PENDING);
        order.setTotalAmount(product.getPrice());

        OrderItem item = new OrderItem();
        item.setProduct(product);
        item.setQuantity(1);
        item.setUnitPrice(product.getPrice());
        item.setSubtotal(product.getPrice());
        order.addItem(item);

        product.setStock(product.getStock() - 1);
        orderRepository.save(order);
    }
}
