package com.example.P06_FetchingLazyEager.controller;

import com.example.P06_FetchingLazyEager.dto.CollectionLazyLoadingResult;
import com.example.P06_FetchingLazyEager.dto.CreateDepartmentRequest;
import com.example.P06_FetchingLazyEager.dto.CreateEmployeeRequest;
import com.example.P06_FetchingLazyEager.dto.DepartmentResponse;
import com.example.P06_FetchingLazyEager.dto.EmployeeBasicResponse;
import com.example.P06_FetchingLazyEager.dto.EmployeeDetailResponse;
import com.example.P06_FetchingLazyEager.dto.LazyExceptionResult;
import com.example.P06_FetchingLazyEager.dto.LazyLoadingResult;
import com.example.P06_FetchingLazyEager.dto.ProxyInspectionResult;
import com.example.P06_FetchingLazyEager.dto.RelationshipInitializationResult;
import com.example.P06_FetchingLazyEager.eagerdemo.dto.CreateEagerDemoRequest;
import com.example.P06_FetchingLazyEager.eagerdemo.dto.EagerLoadingResult;
import com.example.P06_FetchingLazyEager.eagerdemo.service.EagerDemoService;
import com.example.P06_FetchingLazyEager.service.EntityGraphService;
import com.example.P06_FetchingLazyEager.service.FetchJoinService;
import com.example.P06_FetchingLazyEager.service.FetchStrategyService;
import com.example.P06_FetchingLazyEager.service.LazyInitializationExceptionService;
import com.example.P06_FetchingLazyEager.service.LazyLoadingService;
import com.example.P06_FetchingLazyEager.service.PracticeDataService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/practice")
@RequiredArgsConstructor
public class FetchPracticeController {

    private final PracticeDataService practiceDataService;
    private final LazyLoadingService lazyLoadingService;
    private final LazyInitializationExceptionService lazyExceptionService;
    private final FetchStrategyService fetchStrategyService;
    private final FetchJoinService fetchJoinService;
    private final EntityGraphService entityGraphService;
    private final EagerDemoService eagerDemoService;

    // Part 8.1 + 8.2 - Creates Department seed data for the LAZY fetching experiments.
    // Calls PracticeDataService.createDepartment(); no cascade behavior is involved.
    @PostMapping("/departments")
    @ResponseStatus(HttpStatus.CREATED)
    public DepartmentResponse createDepartment(@Valid @RequestBody CreateDepartmentRequest request) {
        return practiceDataService.createDepartment(request);
    }

    // Part 8.2 + 8.4 - Creates Employee seed data whose ManyToOne is explicitly LAZY.
    // Calls PracticeDataService.createEmployee() and saves the owning Employee explicitly.
    @PostMapping("/employees")
    @ResponseStatus(HttpStatus.CREATED)
    public EmployeeBasicResponse createEmployee(@Valid @RequestBody CreateEmployeeRequest request) {
        return practiceDataService.createEmployee(request);
    }

    // Part 8.2 + 8.7 - Loads Employee first, then triggers Department SQL at getName().
    // Calls LazyLoadingService.demonstrateEmployeeLazyDepartment().
    @GetMapping("/lazy/employees/{id}/department")
    public LazyLoadingResult demonstrateLazyDepartment(@PathVariable Long id) {
        return lazyLoadingService.demonstrateEmployeeLazyDepartment(id);
    }

    // Part 8.5 + 8.7 - Loads Department first, then triggers collection SQL at employees.size().
    // Calls LazyLoadingService.demonstrateDepartmentLazyEmployees().
    @GetMapping("/lazy/departments/{id}/employees")
    public CollectionLazyLoadingResult demonstrateLazyEmployees(@PathVariable Long id) {
        return lazyLoadingService.demonstrateDepartmentLazyEmployees(id);
    }

    // Part 8.6 - Inspects lazy runtime class and initialization before/after field access.
    // Calls LazyLoadingService.inspectDepartmentProxy().
    @GetMapping("/proxy/employees/{id}")
    public ProxyInspectionResult inspectProxy(@PathVariable Long id) {
        return lazyLoadingService.inspectDepartmentProxy(id);
    }

    // Part 8.8 - Accesses a lazy Department after the transaction/Persistence Context ended.
    // Calls LazyInitializationExceptionService.demonstrate(); OSIV is disabled.
    @GetMapping("/lazy-exception/employees/{id}")
    public LazyExceptionResult demonstrateLazyInitializationException(@PathVariable Long id) {
        return lazyExceptionService.demonstrate(id);
    }

    // Part 8.10 + 8.11 - Uses JPQL JOIN FETCH to initialize Employee.department.
    // Calls FetchJoinService.findEmployeeDetail().
    @GetMapping("/fetch-join/employees/{id}")
    public EmployeeDetailResponse fetchJoin(@PathVariable Long id) {
        return fetchJoinService.findEmployeeDetail(id);
    }

    // Part 8.12 - Uses @EntityGraph to initialize Department for this repository method.
    // Calls EntityGraphService.findEmployeeDetail().
    @GetMapping("/entity-graph/employees/{id}")
    public EmployeeDetailResponse entityGraph(@PathVariable Long id) {
        return entityGraphService.findEmployeeDetail(id);
    }

    // Part 8.9 + 8.13 - Basic use case deliberately maps only Employee scalar fields.
    // Calls FetchStrategyService.findBasicEmployee(); Department stays lazy.
    @GetMapping("/use-cases/employees/{id}/basic")
    public EmployeeBasicResponse basicUseCase(@PathVariable Long id) {
        return fetchStrategyService.findBasicEmployee(id);
    }

    // Part 8.10 + 8.11 + 8.13 - Detail use case fetches Department with JOIN FETCH.
    // Calls FetchJoinService.findEmployeeDetail().
    @GetMapping("/use-cases/employees/{id}/detail")
    public EmployeeDetailResponse detailUseCase(@PathVariable Long id) {
        return fetchJoinService.findEmployeeDetail(id);
    }

    // Part 8.12 + 8.13 - Same detail shape, fetched declaratively with @EntityGraph.
    // Calls EntityGraphService.findEmployeeDetail().
    @GetMapping("/use-cases/employees/{id}/detail-entity-graph")
    public EmployeeDetailResponse detailEntityGraphUseCase(@PathVariable Long id) {
        return entityGraphService.findEmployeeDetail(id);
    }

    // Part 8.1 + 8.9 - Shows findAll() loads Employees, not every LAZY relationship.
    // Calls FetchStrategyService.inspectFindAll() without reading Department fields.
    @GetMapping("/find-all/initialization")
    public List<RelationshipInitializationResult> inspectFindAll() {
        return fetchStrategyService.inspectFindAll();
    }

    // Part 8.3 + 8.4 - Creates isolated EAGER demo rows and reloads the Employee.
    // Calls EagerDemoService.createDemoData(); EAGER SQL shape is provider-dependent.
    @PostMapping("/eager-demo")
    @ResponseStatus(HttpStatus.CREATED)
    public EagerLoadingResult createEagerDemo(@Valid @RequestBody CreateEagerDemoRequest request) {
        return eagerDemoService.createDemoData(request);
    }

    // Part 8.3 + 8.9 - Loads isolated EagerEmployee and reports initialized state.
    // Calls EagerDemoService.loadEagerEmployee() for comparison with the basic LAZY use case.
    @GetMapping("/eager-demo/employees/{id}")
    public EagerLoadingResult loadEagerEmployee(@PathVariable Long id) {
        return eagerDemoService.loadEagerEmployee(id);
    }
}
