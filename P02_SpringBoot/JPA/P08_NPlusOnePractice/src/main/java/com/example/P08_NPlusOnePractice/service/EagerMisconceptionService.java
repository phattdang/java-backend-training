package com.example.P08_NPlusOnePractice.service;

import com.example.P08_NPlusOnePractice.dto.EmployeeBasicResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class EagerMisconceptionService {

    private final ProjectionService projectionService;

    @Transactional(readOnly = true)
    public Map<String, Object> employeeBasicInfoDoesNotNeedDepartment() {
        List<EmployeeBasicResponse> employees = projectionService.employeeBasicInfo();

        return Map.of(
                "useCase", "GET employee basic info",
                "neededFields", List.of("id", "name", "email"),
                "notNeeded", List.of("Department details"),
                "whyEagerEverywhereIsWasteful", List.of(
                        "unrelated use cases load unnecessary relationships",
                        "query behavior becomes harder to control",
                        "memory and data transfer can increase",
                        "EAGER does not guarantee one efficient SQL query",
                        "EAGER itself can still lead to additional selects depending on query/loading strategy"
                ),
                "data", employees
        );
    }
}
