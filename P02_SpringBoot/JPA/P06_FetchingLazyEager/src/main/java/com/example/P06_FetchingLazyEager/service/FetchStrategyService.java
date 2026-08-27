package com.example.P06_FetchingLazyEager.service;

import com.example.P06_FetchingLazyEager.dto.EmployeeBasicResponse;
import com.example.P06_FetchingLazyEager.dto.RelationshipInitializationResult;
import com.example.P06_FetchingLazyEager.entity.Employee;
import com.example.P06_FetchingLazyEager.repository.EmployeeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.Hibernate;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class FetchStrategyService {

    private final EmployeeRepository employeeRepository;

    @Transactional(readOnly = true)
    public EmployeeBasicResponse findBasicEmployee(Long id) {
        Employee employee = findEmployee(id);
        log.info("Basic use case: department initialized? {}",
                Hibernate.isInitialized(employee.getDepartment()));

        // Mapping only scalar Employee fields intentionally leaves Department untouched.
        return new EmployeeBasicResponse(employee.getId(), employee.getName(), employee.getEmail());
    }

    @Transactional(readOnly = true)
    public List<RelationshipInitializationResult> inspectFindAll() {
        return employeeRepository.findAll().stream()
                .map(employee -> new RelationshipInitializationResult(
                        employee.getId(),
                        employee.getName(),
                        Hibernate.isInitialized(employee.getDepartment())))
                .toList();
    }

    private Employee findEmployee(Long id) {
        return employeeRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Employee not found: " + id));
    }
}
