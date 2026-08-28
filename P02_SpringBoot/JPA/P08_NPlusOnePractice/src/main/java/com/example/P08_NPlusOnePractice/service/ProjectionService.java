package com.example.P08_NPlusOnePractice.service;

import com.example.P08_NPlusOnePractice.dto.EmployeeBasicResponse;
import com.example.P08_NPlusOnePractice.dto.EmployeeDepartmentView;
import com.example.P08_NPlusOnePractice.dto.ExperimentResponse;
import com.example.P08_NPlusOnePractice.dto.QueryExperimentResult;
import com.example.P08_NPlusOnePractice.repository.EmployeeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProjectionService {

    private final EmployeeRepository employeeRepository;
    private final QueryCounter queryCounter;
    private final ResponseMapper responseMapper;

    @Transactional(readOnly = true)
    public ExperimentResponse<EmployeeDepartmentView> employeeDepartmentViews() {
        queryCounter.reset();

        List<EmployeeDepartmentView> data = employeeRepository.findEmployeeDepartmentViews();

        QueryExperimentResult result = new QueryExperimentResult(
                "DTO projection",
                queryCounter.preparedStatementCount(),
                data.size(),
                "The query selects only the required read fields and constructs DTOs instead of loading a managed Entity graph."
        );

        return new ExperimentResponse<>(result, data);
    }

    @Transactional(readOnly = true)
    public List<EmployeeBasicResponse> employeeBasicInfo() {
        return employeeRepository.findAll().stream()
                .map(responseMapper::toEmployeeBasicResponse)
                .toList();
    }
}
