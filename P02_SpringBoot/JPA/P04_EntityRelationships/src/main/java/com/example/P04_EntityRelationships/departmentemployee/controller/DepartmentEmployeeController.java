package com.example.P04_EntityRelationships.departmentemployee.controller;

import com.example.P04_EntityRelationships.departmentemployee.dto.CreateDepartmentRequest;
import com.example.P04_EntityRelationships.departmentemployee.dto.CreateEmployeeRequest;
import com.example.P04_EntityRelationships.departmentemployee.dto.DepartmentResponse;
import com.example.P04_EntityRelationships.departmentemployee.dto.EmployeeResponse;
import com.example.P04_EntityRelationships.departmentemployee.service.DepartmentEmployeeService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/practice")
public class DepartmentEmployeeController {

    private final DepartmentEmployeeService service;

    public DepartmentEmployeeController(DepartmentEmployeeService service) {
        this.service = service;
    }

    // Part 7.1 + 7.6
    // Creates the parent used by the Department 1-N Employee relationship scenario.
    @PostMapping("/departments")
    @ResponseStatus(HttpStatus.CREATED)
    public DepartmentResponse createDepartment(@Valid @RequestBody CreateDepartmentRequest request) {
        return service.createDepartment(request);
    }

    // Part 7.2 + 7.3 + 7.7 + 7.9 + 7.13
    // Employee is the owning side; service.createEmployeeOnOwningSide() writes employees.department_id.
    @PostMapping("/departments/{departmentId}/employees")
    @ResponseStatus(HttpStatus.CREATED)
    public EmployeeResponse createEmployee(
            @PathVariable Long departmentId,
            @Valid @RequestBody CreateEmployeeRequest request) {
        return service.createEmployeeOnOwningSide(departmentId, request);
    }

    // Part 7.11 + 7.14 + 7.15
    // service.createEmployeeWithHelper() calls Department.addEmployee() to synchronize both Java sides.
    @PostMapping("/departments/{departmentId}/employees/helper")
    @ResponseStatus(HttpStatus.CREATED)
    public EmployeeResponse createEmployeeWithHelper(
            @PathVariable Long departmentId,
            @Valid @RequestBody CreateEmployeeRequest request) {
        return service.createEmployeeWithHelper(departmentId, request);
    }

    // Part 7.14 + 7.15
    // service.removeEmployeeWithHelper() clears both Java references, then explicitly saves the owning side.
    @DeleteMapping("/departments/{departmentId}/employees/{employeeId}/helper")
    public EmployeeResponse removeEmployeeWithHelper(
            @PathVariable Long departmentId,
            @PathVariable Long employeeId) {
        return service.removeEmployeeWithHelper(departmentId, employeeId);
    }

    // Part 7.4 + 7.6 + 7.10 + 7.11
    // Reads Department.employees, the inverse side mappedBy Employee.department, and returns a DTO.
    @GetMapping("/departments/{departmentId}")
    public DepartmentResponse findDepartment(@PathVariable Long departmentId) {
        return service.findDepartmentWithEmployees(departmentId);
    }

    // Part 7.2 + 7.3 + 7.7 + 7.13
    // Reads Employee.department from the owning side without serializing a recursive entity graph.
    @GetMapping("/employees/{employeeId}")
    public EmployeeResponse findEmployee(@PathVariable Long employeeId) {
        return service.findEmployeeById(employeeId);
    }
}
