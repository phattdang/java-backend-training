package com.example.P04_EntityRelationships.departmentemployee.service;

import com.example.P04_EntityRelationships.departmentemployee.dto.CreateDepartmentRequest;
import com.example.P04_EntityRelationships.departmentemployee.dto.CreateEmployeeRequest;
import com.example.P04_EntityRelationships.departmentemployee.dto.DepartmentResponse;
import com.example.P04_EntityRelationships.departmentemployee.dto.EmployeeResponse;
import com.example.P04_EntityRelationships.departmentemployee.entity.Department;
import com.example.P04_EntityRelationships.departmentemployee.entity.Employee;
import com.example.P04_EntityRelationships.departmentemployee.repository.DepartmentRepository;
import com.example.P04_EntityRelationships.departmentemployee.repository.EmployeeRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class DepartmentEmployeeService {

    private final DepartmentRepository departmentRepository;
    private final EmployeeRepository employeeRepository;

    public DepartmentEmployeeService(
            DepartmentRepository departmentRepository,
            EmployeeRepository employeeRepository) {
        this.departmentRepository = departmentRepository;
        this.employeeRepository = employeeRepository;
    }

    public DepartmentResponse createDepartment(CreateDepartmentRequest request) {
        Department department = departmentRepository.save(new Department(request.name()));
        return new DepartmentResponse(department.getId(), department.getName(), List.of());
    }

    public EmployeeResponse createEmployeeOnOwningSide(Long departmentId, CreateEmployeeRequest request) {
        Department department = findDepartment(departmentId);
        Employee employee = new Employee(request.name());

        // Only the owning-side reference controls employees.department_id.
        employee.setDepartment(department);
        Employee savedEmployee = employeeRepository.save(employee);
        return toEmployeeResponse(savedEmployee);
    }

    @Transactional
    public EmployeeResponse createEmployeeWithHelper(Long departmentId, CreateEmployeeRequest request) {
        Department department = findDepartment(departmentId);
        Employee employee = new Employee(request.name());

        // Synchronizes department.employees and employee.department in memory.
        department.addEmployee(employee);
        // There is intentionally no cascade, so the owning Employee is saved explicitly.
        Employee savedEmployee = employeeRepository.save(employee);
        return toEmployeeResponse(savedEmployee);
    }

    @Transactional
    public EmployeeResponse removeEmployeeWithHelper(Long departmentId, Long employeeId) {
        Department department = findDepartment(departmentId);
        Employee employee = findEmployee(employeeId);
        if (employee.getDepartment() == null || !departmentId.equals(employee.getDepartment().getId())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Employee does not belong to department: " + departmentId);
        }

        department.removeEmployee(employee);
        // The owning side is saved explicitly; the helper method itself does not persist anything.
        Employee savedEmployee = employeeRepository.save(employee);
        return toEmployeeResponse(savedEmployee);
    }

    @Transactional(readOnly = true)
    public DepartmentResponse findDepartmentWithEmployees(Long departmentId) {
        Department department = findDepartment(departmentId);
        List<EmployeeResponse> employees = department.getEmployees().stream()
                .map(this::toEmployeeResponse)
                .toList();
        return new DepartmentResponse(department.getId(), department.getName(), employees);
    }

    public EmployeeResponse findEmployeeById(Long employeeId) {
        return toEmployeeResponse(findEmployee(employeeId));
    }

    private Department findDepartment(Long id) {
        return departmentRepository.findById(id)
                .orElseThrow(() -> notFound("Department", id));
    }

    private Employee findEmployee(Long id) {
        return employeeRepository.findById(id)
                .orElseThrow(() -> notFound("Employee", id));
    }

    private EmployeeResponse toEmployeeResponse(Employee employee) {
        Long departmentId = employee.getDepartment() == null ? null : employee.getDepartment().getId();
        return new EmployeeResponse(employee.getId(), employee.getName(), departmentId);
    }

    private ResponseStatusException notFound(String type, Long id) {
        return new ResponseStatusException(HttpStatus.NOT_FOUND, type + " not found: " + id);
    }
}
