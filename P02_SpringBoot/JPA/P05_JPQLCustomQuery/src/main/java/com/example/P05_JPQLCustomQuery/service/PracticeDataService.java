package com.example.P05_JPQLCustomQuery.service;

import com.example.P05_JPQLCustomQuery.dto.CreateDepartmentRequest;
import com.example.P05_JPQLCustomQuery.dto.CreateEmployeeRequest;
import com.example.P05_JPQLCustomQuery.dto.DepartmentResponse;
import com.example.P05_JPQLCustomQuery.dto.EmployeeResponse;
import com.example.P05_JPQLCustomQuery.entity.Department;
import com.example.P05_JPQLCustomQuery.entity.Employee;
import com.example.P05_JPQLCustomQuery.repository.DepartmentRepository;
import com.example.P05_JPQLCustomQuery.repository.EmployeeRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class PracticeDataService {

    private final DepartmentRepository departmentRepository;
    private final EmployeeRepository employeeRepository;

    public PracticeDataService(
            DepartmentRepository departmentRepository,
            EmployeeRepository employeeRepository) {
        this.departmentRepository = departmentRepository;
        this.employeeRepository = employeeRepository;
    }

    public DepartmentResponse createDepartment(CreateDepartmentRequest request) {
        Department department = departmentRepository.save(new Department(request.name()));
        return new DepartmentResponse(department.getId(), department.getName());
    }

    public EmployeeResponse createEmployee(CreateEmployeeRequest request) {
        Department department = departmentRepository.findById(request.departmentId())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Department not found: " + request.departmentId()));
        Employee employee = new Employee(
                request.name(),
                request.email(),
                request.salary(),
                request.active(),
                department
        );
        return EmployeeResponse.from(employeeRepository.save(employee));
    }
}
