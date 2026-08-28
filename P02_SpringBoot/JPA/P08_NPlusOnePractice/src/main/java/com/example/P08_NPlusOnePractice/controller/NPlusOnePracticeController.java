package com.example.P08_NPlusOnePractice.controller;

import com.example.P08_NPlusOnePractice.dto.BatchFetchingExplanation;
import com.example.P08_NPlusOnePractice.dto.CreateDepartmentRequest;
import com.example.P08_NPlusOnePractice.dto.CreateEmployeeRequest;
import com.example.P08_NPlusOnePractice.dto.DepartmentResponse;
import com.example.P08_NPlusOnePractice.dto.EmployeeDepartmentView;
import com.example.P08_NPlusOnePractice.dto.EmployeeResponse;
import com.example.P08_NPlusOnePractice.dto.ExperimentResponse;
import com.example.P08_NPlusOnePractice.service.BatchFetchingService;
import com.example.P08_NPlusOnePractice.service.EagerMisconceptionService;
import com.example.P08_NPlusOnePractice.service.EntityGraphService;
import com.example.P08_NPlusOnePractice.service.FetchJoinService;
import com.example.P08_NPlusOnePractice.service.NPlusOneService;
import com.example.P08_NPlusOnePractice.service.ProjectionService;
import com.example.P08_NPlusOnePractice.service.SeedService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/practice")
@RequiredArgsConstructor
public class NPlusOnePracticeController {

    private final SeedService seedService;
    private final NPlusOneService nPlusOneService;
    private final FetchJoinService fetchJoinService;
    private final EntityGraphService entityGraphService;
    private final ProjectionService projectionService;
    private final BatchFetchingService batchFetchingService;
    private final EagerMisconceptionService eagerMisconceptionService;

    // Part 11.5
    // Creates one Department for manual test data used by the SQL log experiments.
    @PostMapping("/departments")
    public DepartmentResponse createDepartment(@Valid @RequestBody CreateDepartmentRequest request) {
        return seedService.createDepartment(request);
    }

    // Part 11.5
    // Creates one Employee linked to an existing Department for manual N+1 setup.
    @PostMapping("/employees")
    public EmployeeResponse createEmployee(@Valid @RequestBody CreateEmployeeRequest request) {
        return seedService.createEmployee(request);
    }

    // Part 11.5
    // Seeds three Departments with three Employees each so repeated SQL is easy to observe.
    @PostMapping("/seed")
    public List<DepartmentResponse> seed() {
        return seedService.seed();
    }

    // Part 11.1 + 11.2 + 11.3 + 11.5
    // Reproduces OneToMany N+1: one Department query followed by lazy Employee queries per Department.
    @GetMapping("/nplus1/one-to-many")
    public ExperimentResponse<DepartmentResponse> oneToManyNPlusOne() {
        return nPlusOneService.oneToManyNPlusOne();
    }

    // Part 11.4 + 11.5
    // Reproduces ManyToOne N+1 by loading Employees first and then accessing each lazy Department.
    @GetMapping("/nplus1/many-to-one")
    public ExperimentResponse<EmployeeResponse> manyToOneNPlusOne() {
        return nPlusOneService.manyToOneNPlusOne();
    }

    // Part 11.6
    // Solves the OneToMany N+1 use case using JPQL LEFT JOIN FETCH.
    @GetMapping("/solutions/fetch-join/departments")
    public ExperimentResponse<DepartmentResponse> departmentsFetchJoin() {
        return fetchJoinService.departmentsWithEmployees();
    }

    // Part 11.6
    // Solves the ManyToOne N+1 use case using JPQL JOIN FETCH.
    @GetMapping("/solutions/fetch-join/employees")
    public ExperimentResponse<EmployeeResponse> employeesFetchJoin() {
        return fetchJoinService.employeesWithDepartment();
    }

    // Part 11.7
    // Solves the same OneToMany use case using @EntityGraph.
    @GetMapping("/solutions/entity-graph/departments")
    public ExperimentResponse<DepartmentResponse> departmentsEntityGraph() {
        return entityGraphService.departmentsWithEmployees();
    }

    // Part 11.7
    // Solves the same ManyToOne use case using @EntityGraph.
    @GetMapping("/solutions/entity-graph/employees")
    public ExperimentResponse<EmployeeResponse> employeesEntityGraph() {
        return entityGraphService.employeesWithDepartment();
    }

    // Part 11.8
    // Uses DTO Projection to query only the fields required by the read use case.
    @GetMapping("/solutions/projection/employees")
    public ExperimentResponse<EmployeeDepartmentView> employeeProjection() {
        return projectionService.employeeDepartmentViews();
    }

    // Part 11.9
    // Explains Hibernate batch fetching without enabling it globally and hiding baseline N+1.
    @GetMapping("/solutions/batch-fetching")
    public BatchFetchingExplanation batchFetching() {
        return batchFetchingService.explainBatchFetching();
    }

    // Part 11.10
    // Demonstrates why globally changing relationships to EAGER is not the preferred solution.
    @GetMapping("/eager-is-not-solution/employees/basic")
    public Map<String, Object> eagerIsNotSolution() {
        return eagerMisconceptionService.employeeBasicInfoDoesNotNeedDepartment();
    }

    // Part 11.1 + 11.2 + 11.3 + 11.5
    // Compare endpoint for the broken OneToMany baseline.
    @GetMapping("/compare/one-to-many/nplus1")
    public ExperimentResponse<DepartmentResponse> compareOneToManyNPlusOne() {
        return nPlusOneService.oneToManyNPlusOne();
    }

    // Part 11.6
    // Compare endpoint for the OneToMany fetch join solution.
    @GetMapping("/compare/one-to-many/fetch-join")
    public ExperimentResponse<DepartmentResponse> compareOneToManyFetchJoin() {
        return fetchJoinService.departmentsWithEmployees();
    }

    // Part 11.7
    // Compare endpoint for the OneToMany @EntityGraph solution.
    @GetMapping("/compare/one-to-many/entity-graph")
    public ExperimentResponse<DepartmentResponse> compareOneToManyEntityGraph() {
        return entityGraphService.departmentsWithEmployees();
    }

    // Part 11.4 + 11.5
    // Compare endpoint for the broken ManyToOne baseline.
    @GetMapping("/compare/many-to-one/nplus1")
    public ExperimentResponse<EmployeeResponse> compareManyToOneNPlusOne() {
        return nPlusOneService.manyToOneNPlusOne();
    }

    // Part 11.6
    // Compare endpoint for the ManyToOne fetch join solution.
    @GetMapping("/compare/many-to-one/fetch-join")
    public ExperimentResponse<EmployeeResponse> compareManyToOneFetchJoin() {
        return fetchJoinService.employeesWithDepartment();
    }

    // Part 11.7
    // Compare endpoint for the ManyToOne @EntityGraph solution.
    @GetMapping("/compare/many-to-one/entity-graph")
    public ExperimentResponse<EmployeeResponse> compareManyToOneEntityGraph() {
        return entityGraphService.employeesWithDepartment();
    }

    // Part 11.8
    // Compare endpoint for DTO Projection: one read query, no managed relationship graph.
    @GetMapping("/compare/projection")
    public ExperimentResponse<EmployeeDepartmentView> compareProjection() {
        return projectionService.employeeDepartmentViews();
    }
}
