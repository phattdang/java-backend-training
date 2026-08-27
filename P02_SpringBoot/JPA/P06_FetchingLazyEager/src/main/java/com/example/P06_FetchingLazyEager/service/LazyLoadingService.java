package com.example.P06_FetchingLazyEager.service;

import com.example.P06_FetchingLazyEager.dto.CollectionLazyLoadingResult;
import com.example.P06_FetchingLazyEager.dto.LazyLoadingResult;
import com.example.P06_FetchingLazyEager.dto.ProxyInspectionResult;
import com.example.P06_FetchingLazyEager.entity.Department;
import com.example.P06_FetchingLazyEager.entity.Employee;
import com.example.P06_FetchingLazyEager.repository.DepartmentRepository;
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
public class LazyLoadingService {

    private final EmployeeRepository employeeRepository;
    private final DepartmentRepository departmentRepository;

    @Transactional(readOnly = true)
    public LazyLoadingResult demonstrateEmployeeLazyDepartment(Long id) {
        log.info("=== START LAZY MANY-TO-ONE TEST ===");
        Employee employee = findEmployee(id);
        log.info("=== EMPLOYEE LOADED ===");

        boolean before = Hibernate.isInitialized(employee.getDepartment());
        log.info("=== DEPARTMENT INITIALIZED? {} ===", before);
        log.info("=== ACCESSING DEPARTMENT.NAME ===");
        String departmentName = employee.getDepartment().getName();
        boolean after = Hibernate.isInitialized(employee.getDepartment());
        log.info("=== DEPARTMENT INITIALIZED? {} ===", after);

        return new LazyLoadingResult(
                employee.getId(), employee.getName(), before, after, departmentName);
    }

    @Transactional(readOnly = true)
    public CollectionLazyLoadingResult demonstrateDepartmentLazyEmployees(Long id) {
        Department department = findDepartment(id);
        log.info("=== DEPARTMENT LOADED ===");

        boolean before = Hibernate.isInitialized(department.getEmployees());
        log.info("=== EMPLOYEES INITIALIZED? {} ===", before);
        log.info("=== ACCESSING EMPLOYEES.SIZE ===");
        int employeeCount = department.getEmployees().size();
        boolean after = Hibernate.isInitialized(department.getEmployees());
        log.info("=== EMPLOYEES INITIALIZED? {} ===", after);

        List<String> names = department.getEmployees().stream()
                .map(Employee::getName)
                .toList();
        return new CollectionLazyLoadingResult(
                department.getId(), department.getName(), before, after, employeeCount, names);
    }

    @Transactional(readOnly = true)
    public ProxyInspectionResult inspectDepartmentProxy(Long id) {
        Employee employee = findEmployee(id);
        Department department = employee.getDepartment();
        String runtimeClass = department.getClass().getName();
        boolean before = Hibernate.isInitialized(department);
        log.info("Lazy Department runtime class: {}", runtimeClass);
        log.info("=== DEPARTMENT INITIALIZED? {} ===", before);

        String departmentName = department.getName();
        boolean after = Hibernate.isInitialized(department);
        log.info("=== DEPARTMENT INITIALIZED? {} ===", after);
        return new ProxyInspectionResult(runtimeClass, before, after, departmentName);
    }

    private Employee findEmployee(Long id) {
        return employeeRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Employee not found: " + id));
    }

    private Department findDepartment(Long id) {
        return departmentRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Department not found: " + id));
    }
}
