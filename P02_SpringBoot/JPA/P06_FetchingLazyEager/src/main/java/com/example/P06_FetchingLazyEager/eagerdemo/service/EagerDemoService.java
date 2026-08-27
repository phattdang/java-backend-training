package com.example.P06_FetchingLazyEager.eagerdemo.service;

import com.example.P06_FetchingLazyEager.eagerdemo.dto.CreateEagerDemoRequest;
import com.example.P06_FetchingLazyEager.eagerdemo.dto.EagerLoadingResult;
import com.example.P06_FetchingLazyEager.eagerdemo.entity.EagerDepartment;
import com.example.P06_FetchingLazyEager.eagerdemo.entity.EagerEmployee;
import com.example.P06_FetchingLazyEager.eagerdemo.repository.EagerDepartmentRepository;
import com.example.P06_FetchingLazyEager.eagerdemo.repository.EagerEmployeeRepository;
import jakarta.persistence.EntityManager;
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
public class EagerDemoService {

    private final EagerDepartmentRepository departmentRepository;
    private final EagerEmployeeRepository employeeRepository;
    private final EntityManager entityManager;

    @Transactional
    public EagerLoadingResult createDemoData(CreateEagerDemoRequest request) {
        EagerDepartment department = departmentRepository.save(
                new EagerDepartment(request.departmentName()));
        EagerEmployee employee = employeeRepository.save(new EagerEmployee(
                request.employeeName(), request.employeeEmail(), request.salary(), department));

        // Clear first so the returned result comes from a real EAGER reload, not the assigned Java reference.
        entityManager.flush();
        entityManager.clear();
        return loadFromDatabase(employee.getId());
    }

    @Transactional(readOnly = true)
    public EagerLoadingResult loadEagerEmployee(Long id) {
        return loadFromDatabase(id);
    }

    private EagerLoadingResult loadFromDatabase(Long id) {
        log.info("=== ABOUT TO LOAD EAGER EMPLOYEE ===");
        EagerEmployee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Eager demo employee not found: " + id));
        boolean initialized = Hibernate.isInitialized(employee.getDepartment());
        log.info("=== EAGER EMPLOYEE LOADED; DEPARTMENT INITIALIZED? {} ===", initialized);

        return new EagerLoadingResult(
                employee.getId(),
                employee.getName(),
                employee.getDepartment().getId(),
                employee.getDepartment().getName(),
                initialized);
    }
}
