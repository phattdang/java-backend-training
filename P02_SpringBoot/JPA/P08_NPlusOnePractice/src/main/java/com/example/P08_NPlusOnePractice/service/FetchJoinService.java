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
public class FetchJoinService {

    private final DepartmentRepository departmentRepository;
    private final EmployeeRepository employeeRepository;
    private final QueryCounter queryCounter;
    private final ResponseMapper responseMapper;

    @Transactional(readOnly = true)
    public ExperimentResponse<DepartmentResponse> departmentsWithEmployees() {
        queryCounter.reset();

        List<Department> departments = departmentRepository.findAllWithEmployeesFetchJoin();
        log.info("=== FETCH JOIN QUERY EXECUTED ===");
        log.info("=== ACCESSING EMPLOYEES AFTER FETCH JOIN ===");

        List<DepartmentResponse> data = departments.stream()
                .map(responseMapper::toDepartmentResponse)
                .toList();

        QueryExperimentResult result = new QueryExperimentResult(
                "OneToMany solved with JPQL fetch join",
                queryCounter.preparedStatementCount(),
                departments.size(),
                "The query fetches Departments and Employees for this use case. DISTINCT avoids duplicate Department root results from the collection join."
        );

        return new ExperimentResponse<>(result, data);
    }

    @Transactional(readOnly = true)
    public ExperimentResponse<EmployeeResponse> employeesWithDepartment() {
        queryCounter.reset();

        List<Employee> employees = employeeRepository.findAllWithDepartmentFetchJoin();
        log.info("=== FETCH JOIN QUERY EXECUTED ===");
        log.info("=== ACCESSING DEPARTMENTS AFTER FETCH JOIN ===");

        List<EmployeeResponse> data = employees.stream()
                .map(responseMapper::toEmployeeResponse)
                .toList();

        QueryExperimentResult result = new QueryExperimentResult(
                "ManyToOne solved with JPQL fetch join",
                queryCounter.preparedStatementCount(),
                employees.size(),
                "The query fetches each Employee with its Department, so DTO mapping does not trigger per-Employee lazy SELECTs."
        );

        return new ExperimentResponse<>(result, data);
    }
}
