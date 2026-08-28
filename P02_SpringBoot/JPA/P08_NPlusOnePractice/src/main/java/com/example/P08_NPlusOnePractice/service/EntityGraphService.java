package com.example.P08_NPlusOnePractice.service;

import com.example.P08_NPlusOnePractice.dto.DepartmentResponse;
import com.example.P08_NPlusOnePractice.dto.EmployeeResponse;
import com.example.P08_NPlusOnePractice.dto.ExperimentResponse;
import com.example.P08_NPlusOnePractice.dto.QueryExperimentResult;
import com.example.P08_NPlusOnePractice.entity.Department;
import com.example.P08_NPlusOnePractice.entity.Employee;
import com.example.P08_NPlusOnePractice.repository.DepartmentRepository;
import com.example.P08_NPlusOnePractice.repository.EmployeeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class EntityGraphService {

    private final DepartmentRepository departmentRepository;
    private final EmployeeRepository employeeRepository;
    private final QueryCounter queryCounter;
    private final ResponseMapper responseMapper;

    @Transactional(readOnly = true)
    public ExperimentResponse<DepartmentResponse> departmentsWithEmployees() {
        queryCounter.reset();

        List<Department> departments = departmentRepository.findAllWithEmployeesEntityGraph();
        log.info("=== ENTITY GRAPH QUERY EXECUTED ===");
        log.info("=== ACCESSING EMPLOYEES AFTER ENTITY GRAPH ===");

        List<DepartmentResponse> data = departments.stream()
                .map(responseMapper::toDepartmentResponse)
                .toList();

        QueryExperimentResult result = new QueryExperimentResult(
                "OneToMany solved with @EntityGraph",
                queryCounter.preparedStatementCount(),
                departments.size(),
                "@EntityGraph declares the fetch plan on this repository method, so Employees are loaded for this use case without changing the mapping to EAGER."
        );

        return new ExperimentResponse<>(result, data);
    }

    @Transactional(readOnly = true)
    public ExperimentResponse<EmployeeResponse> employeesWithDepartment() {
        queryCounter.reset();

        List<Employee> employees = employeeRepository.findAllWithDepartmentEntityGraph();
        log.info("=== ENTITY GRAPH QUERY EXECUTED ===");
        log.info("=== ACCESSING DEPARTMENTS AFTER ENTITY GRAPH ===");

        List<EmployeeResponse> data = employees.stream()
                .map(responseMapper::toEmployeeResponse)
                .toList();

        QueryExperimentResult result = new QueryExperimentResult(
                "ManyToOne solved with @EntityGraph",
                queryCounter.preparedStatementCount(),
                employees.size(),
                "@EntityGraph loads Department for this Employee query only. Other Employee queries still keep the LAZY mapping."
        );

        return new ExperimentResponse<>(result, data);
    }
}
