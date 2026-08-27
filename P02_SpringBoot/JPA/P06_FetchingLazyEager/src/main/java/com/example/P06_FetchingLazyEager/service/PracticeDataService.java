package com.example.P06_FetchingLazyEager.service;

import com.example.P06_FetchingLazyEager.dto.CreateDepartmentRequest;
import com.example.P06_FetchingLazyEager.dto.CreateEmployeeRequest;
import com.example.P06_FetchingLazyEager.dto.DepartmentResponse;
import com.example.P06_FetchingLazyEager.dto.EmployeeBasicResponse;
import com.example.P06_FetchingLazyEager.entity.Department;
import com.example.P06_FetchingLazyEager.entity.Employee;
import com.example.P06_FetchingLazyEager.repository.DepartmentRepository;
import com.example.P06_FetchingLazyEager.repository.EmployeeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class PracticeDataService {

    private final DepartmentRepository departmentRepository;
    private final EmployeeRepository employeeRepository;

    @Transactional
    public DepartmentResponse createDepartment(CreateDepartmentRequest request) {
        Department saved = departmentRepository.save(
                new Department(request.name(), request.description()));
        return new DepartmentResponse(saved.getId(), saved.getName(), saved.getDescription());
    }

    @Transactional
    public EmployeeBasicResponse createEmployee(CreateEmployeeRequest request) {
        Department department = departmentRepository.findById(request.departmentId())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Department not found: " + request.departmentId()));

        // No cascade is used: both entities are saved explicitly in this laboratory.
        Employee saved = employeeRepository.save(new Employee(
                request.name(), request.email(), request.salary(), department));
        return new EmployeeBasicResponse(saved.getId(), saved.getName(), saved.getEmail());
    }
}
