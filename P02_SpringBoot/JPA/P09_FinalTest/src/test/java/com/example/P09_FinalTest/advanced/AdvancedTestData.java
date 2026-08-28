package com.example.P09_FinalTest.advanced;

import com.example.P09_FinalTest.entity.Customer;
import com.example.P09_FinalTest.entity.Product;
import com.example.P09_FinalTest.entity.enums.CustomerStatus;
import com.example.P09_FinalTest.entity.enums.ProductStatus;

import java.math.BigDecimal;
import java.util.UUID;

final class AdvancedTestData {
    private AdvancedTestData() {
    }

    static String suffix() {
        return UUID.randomUUID().toString().substring(0, 8);
    }

    static Customer customer(String suffix) {
        Customer customer = new Customer();
        customer.setFullName("Advanced Customer " + suffix);
        customer.setEmail("advanced-" + suffix + "@example.com");
        customer.setPhone("09" + Math.abs(suffix.hashCode()));
        customer.setStatus(CustomerStatus.ACTIVE);
        return customer;
    }

    static Product product(String suffix, int stock) {
        Product product = new Product();
        product.setProductCode("ADV-" + suffix);
        product.setName("Advanced Product " + suffix);
        product.setDescription("Product created by advanced integration tests");
        product.setPrice(new BigDecimal("100.00"));
        product.setStock(stock);
        product.setStatus(ProductStatus.ACTIVE);
        return product;
    }
}
