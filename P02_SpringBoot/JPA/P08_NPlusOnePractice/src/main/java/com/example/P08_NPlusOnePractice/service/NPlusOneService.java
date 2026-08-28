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
public class NPlusOneService {

    private final DepartmentRepository departmentRepository;
    private final EmployeeRepository employeeRepository;
    private final QueryCounter queryCounter;
    private final ResponseMapper responseMapper;

    @Transactional(readOnly = true)
    public ExperimentResponse<DepartmentResponse> oneToManyNPlusOne() {
        queryCounter.reset();

        List<Department> departments = departmentRepository.findAll();
        log.info("=== ROOT DEPARTMENTS LOADED ===");

        List<DepartmentResponse> data = departments.stream()
                .map(department -> {
                    log.info("=== ACCESSING EMPLOYEES FOR DEPARTMENT {} ===", department.getId());
                    department.getEmployees().size();
                    return responseMapper.toDepartmentResponse(department);
                })
                .toList();

        QueryExperimentResult result = new QueryExperimentResult(
                "OneToMany N+1 baseline",
                queryCounter.preparedStatementCount(),
                departments.size(),
                "findAll() loads Departments first; accessing each LAZY employees collection in the loop can create one extra SELECT per Department."
        );

        return new ExperimentResponse<>(result, data);
    }

    @Transactional(readOnly = true)
    public ExperimentResponse<EmployeeResponse> manyToOneNPlusOne() {
        queryCounter.reset();

        List<Employee> employees = employeeRepository.findAll();
        log.info("=== ROOT EMPLOYEES LOADED ===");

        List<EmployeeResponse> data = employees.stream()
                .map(employee -> {
                    log.info("=== ACCESSING DEPARTMENT FOR EMPLOYEE {} ===", employee.getId());
                    employee.getDepartment().getName();
                    return responseMapper.toEmployeeResponse(employee);
                })
                .toList();

        QueryExperimentResult result = new QueryExperimentResult(
                "ManyToOne N+1 baseline",
                queryCounter.preparedStatementCount(),
                employees.size(),
                "findAll() loads Employees first; accessing each LAZY department can create repeated SELECTs. The count may be lower than employee count when several Employees share a Department already in the first-level cache."
        );

        return new ExperimentResponse<>(result, data);
    }
}
