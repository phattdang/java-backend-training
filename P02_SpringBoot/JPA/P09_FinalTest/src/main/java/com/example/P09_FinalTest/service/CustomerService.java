package com.example.P09_FinalTest.service;

import com.example.P09_FinalTest.dto.customer.CreateCustomerRequest;
import com.example.P09_FinalTest.dto.customer.CustomerResponse;
import com.example.P09_FinalTest.entity.Customer;
import com.example.P09_FinalTest.entity.enums.CustomerStatus;
import com.example.P09_FinalTest.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class CustomerService {
    private final CustomerRepository customerRepository;

    @Transactional
    public CustomerResponse createCustomer(CreateCustomerRequest request) {
        if (customerRepository.existsByEmail(request.email())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Email already exists");
        }

        Customer customer = new Customer();
        customer.setFullName(request.fullName());
        customer.setEmail(request.email());
        customer.setPhone(request.phone());
        customer.setStatus(CustomerStatus.ACTIVE);

        return toResponse(customerRepository.save(customer));
    }

    @Transactional(readOnly = true)
    public CustomerResponse getCustomer(Long id) {
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Customer not found"));

        return toResponse(customer);
    }

    private CustomerResponse toResponse(Customer customer) {
        return new CustomerResponse(
                customer.getId(),
                customer.getFullName(),
                customer.getEmail(),
                customer.getPhone(),
                customer.getStatus(),
                customer.getCreatedAt(),
                customer.getUpdatedAt()
        );
    }
}
