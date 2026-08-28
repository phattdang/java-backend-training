package com.example.P08_NPlusOnePractice.service;

import com.example.P08_NPlusOnePractice.dto.CreateDepartmentRequest;
import com.example.P08_NPlusOnePractice.dto.CreateEmployeeRequest;
import com.example.P08_NPlusOnePractice.dto.DepartmentResponse;
import com.example.P08_NPlusOnePractice.dto.EmployeeResponse;
import com.example.P08_NPlusOnePractice.entity.Department;
import com.example.P08_NPlusOnePractice.entity.Employee;
import com.example.P08_NPlusOnePractice.repository.DepartmentRepository;
import com.example.P08_NPlusOnePractice.repository.EmployeeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SeedService {

    private final DepartmentRepository departmentRepository;
    private final EmployeeRepository employeeRepository;
    private final ResponseMapper responseMapper;

    @Transactional
    public DepartmentResponse createDepartment(CreateDepartmentRequest request) {
        Department department = departmentRepository.save(new Department(request.name()));
        return responseMapper.toDepartmentResponse(department);
    }

    @Transactional
    public EmployeeResponse createEmployee(CreateEmployeeRequest request) {
        Department department = departmentRepository.findById(request.departmentId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Department not found"));

        Employee employee = new Employee(request.name(), request.email(), request.salary());
        department.addEmployee(employee);
        employeeRepository.save(employee);

        return responseMapper.toEmployeeResponse(employee);
    }

    @Transactional
    public List<DepartmentResponse> seed() {
        Department departmentA = departmentRepository.save(new Department("Department A"));
        Employee employeeA1 = new Employee("Employee A1", "a1@example.com", new BigDecimal("1000.00"));
        Employee employeeA2 = new Employee("Employee A2", "a2@example.com", new BigDecimal("1200.00"));
        Employee employeeA3 = new Employee("Employee A3", "a3@example.com", new BigDecimal("1400.00"));
        departmentA.addEmployee(employeeA1);
        departmentA.addEmployee(employeeA2);
        departmentA.addEmployee(employeeA3);
        employeeRepository.saveAll(List.of(employeeA1, employeeA2, employeeA3));

        Department departmentB = departmentRepository.save(new Department("Department B"));
        Employee employeeB1 = new Employee("Employee B1", "b1@example.com", new BigDecimal("1600.00"));
        Employee employeeB2 = new Employee("Employee B2", "b2@example.com", new BigDecimal("1800.00"));
        Employee employeeB3 = new Employee("Employee B3", "b3@example.com", new BigDecimal("2000.00"));
        departmentB.addEmployee(employeeB1);
        departmentB.addEmployee(employeeB2);
        departmentB.addEmployee(employeeB3);
        employeeRepository.saveAll(List.of(employeeB1, employeeB2, employeeB3));

        Department departmentC = departmentRepository.save(new Department("Department C"));
        Employee employeeC1 = new Employee("Employee C1", "c1@example.com", new BigDecimal("2200.00"));
        Employee employeeC2 = new Employee("Employee C2", "c2@example.com", new BigDecimal("2400.00"));
        Employee employeeC3 = new Employee("Employee C3", "c3@example.com", new BigDecimal("2600.00"));
        departmentC.addEmployee(employeeC1);
        departmentC.addEmployee(employeeC2);
        departmentC.addEmployee(employeeC3);
        employeeRepository.saveAll(List.of(employeeC1, employeeC2, employeeC3));

        return List.of(departmentA, departmentB, departmentC).stream()
                .map(responseMapper::toDepartmentResponse)
                .toList();
    }
}
