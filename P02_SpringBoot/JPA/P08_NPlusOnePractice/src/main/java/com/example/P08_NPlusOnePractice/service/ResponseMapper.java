package com.example.P08_NPlusOnePractice.service;

import com.example.P08_NPlusOnePractice.dto.DepartmentResponse;
import com.example.P08_NPlusOnePractice.dto.EmployeeBasicResponse;
import com.example.P08_NPlusOnePractice.dto.EmployeeResponse;
import com.example.P08_NPlusOnePractice.dto.EmployeeSummary;
import com.example.P08_NPlusOnePractice.entity.Department;
import com.example.P08_NPlusOnePractice.entity.Employee;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ResponseMapper {

    public DepartmentResponse toDepartmentResponse(Department department) {
        // In baseline experiments this DTO mapping deliberately reads the lazy collection.
        // That relationship access is what makes the N+1 pattern visible in SQL logs.
        List<EmployeeSummary> employees = department.getEmployees().stream()
                .map(this::toEmployeeSummary)
                .toList();

        return new DepartmentResponse(
                department.getId(),
                department.getName(),
                employees
        );
    }

    public EmployeeResponse toEmployeeResponse(Employee employee) {
        // In baseline experiments this DTO mapping deliberately reads the lazy to-one relation.
        // Fetch join and EntityGraph endpoints keep the same response shape but pre-load it.
        return new EmployeeResponse(
                employee.getId(),
                employee.getName(),
                employee.getEmail(),
                employee.getDepartment().getId(),
                employee.getDepartment().getName()
        );
    }

    public EmployeeBasicResponse toEmployeeBasicResponse(Employee employee) {
        return new EmployeeBasicResponse(
                employee.getId(),
                employee.getName(),
                employee.getEmail()
        );
    }

    private EmployeeSummary toEmployeeSummary(Employee employee) {
        return new EmployeeSummary(
                employee.getId(),
                employee.getName(),
                employee.getEmail()
        );
    }
}
