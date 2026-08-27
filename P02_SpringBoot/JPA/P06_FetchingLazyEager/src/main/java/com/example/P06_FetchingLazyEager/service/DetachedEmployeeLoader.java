package com.example.P06_FetchingLazyEager.service;

import com.example.P06_FetchingLazyEager.entity.Employee;
import com.example.P06_FetchingLazyEager.repository.EmployeeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.Hibernate;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
@Slf4j
public class DetachedEmployeeLoader {

    private final EmployeeRepository employeeRepository;

    @Transactional(readOnly = true)
    public Employee loadEmployeeWithoutDepartment(Long id) {
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Employee not found: " + id));
        log.info("Returning Employee while Department initialized? {}",
                Hibernate.isInitialized(employee.getDepartment()));
        return employee;
    }
}
