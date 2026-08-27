package com.example.P05_JPQLCustomQuery.service;

import com.example.P05_JPQLCustomQuery.dto.AggregateResult;
import com.example.P05_JPQLCustomQuery.dto.DepartmentStats;
import com.example.P05_JPQLCustomQuery.dto.EmployeeResponse;
import com.example.P05_JPQLCustomQuery.dto.EmployeeSummary;
import com.example.P05_JPQLCustomQuery.entity.Employee;
import com.example.P05_JPQLCustomQuery.repository.EmployeeRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class EmployeeQueryService {

    private final EmployeeRepository employeeRepository;

    public EmployeeQueryService(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    public List<EmployeeResponse> findAllJpql() {
        return toResponses(employeeRepository.findAllEmployeesJpql());
    }

    public List<EmployeeResponse> findActiveJpql() {
        return toResponses(employeeRepository.findActiveEmployees());
    }

    public List<EmployeeResponse> findPositional(String name, boolean active) {
        return toResponses(employeeRepository.findByNameAndActivePositional(name, active));
    }

    public List<EmployeeResponse> findNamed(String name, boolean active) {
        return toResponses(employeeRepository.findByNameAndActiveNamed(name, active));
    }

    public List<EmployeeResponse> findByDepartmentName(String departmentName) {
        return toResponses(employeeRepository.findEmployeesByDepartmentName(departmentName));
    }

    public AggregateResult aggregate() {
        BigDecimal totalSalary = employeeRepository.sumEmployeeSalary();
        Double averageSalary = employeeRepository.averageEmployeeSalary();
        return new AggregateResult(
                employeeRepository.countEmployeesJpql(),
                totalSalary == null ? BigDecimal.ZERO : totalSalary,
                averageSalary == null ? 0.0 : averageSalary
        );
    }

    public List<EmployeeSummary> findEmployeeSummaries() {
        return employeeRepository.findEmployeeSummaries();
    }

    public List<DepartmentStats> findDepartmentStats() {
        return employeeRepository.findDepartmentStats();
    }

    public List<EmployeeResponse> findActiveNative() {
        return toResponses(employeeRepository.findActiveEmployeesNative());
    }

    private List<EmployeeResponse> toResponses(List<Employee> employees) {
        return employees.stream()
                .map(EmployeeResponse::from)
                .toList();
    }
}
