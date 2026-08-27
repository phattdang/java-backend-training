package com.example.P05_JPQLCustomQuery.controller;

import com.example.P05_JPQLCustomQuery.dto.AffectedRowsResult;
import com.example.P05_JPQLCustomQuery.dto.AggregateResult;
import com.example.P05_JPQLCustomQuery.dto.AutoClearBulkUpdateResult;
import com.example.P05_JPQLCustomQuery.dto.BulkUpdateResult;
import com.example.P05_JPQLCustomQuery.dto.CreateDepartmentRequest;
import com.example.P05_JPQLCustomQuery.dto.CreateEmployeeRequest;
import com.example.P05_JPQLCustomQuery.dto.DepartmentResponse;
import com.example.P05_JPQLCustomQuery.dto.DepartmentStats;
import com.example.P05_JPQLCustomQuery.dto.EmployeeResponse;
import com.example.P05_JPQLCustomQuery.dto.EmployeeSummary;
import com.example.P05_JPQLCustomQuery.dto.UpdateNameRequest;
import com.example.P05_JPQLCustomQuery.dto.UpdateSalaryRequest;
import com.example.P05_JPQLCustomQuery.service.BulkQueryService;
import com.example.P05_JPQLCustomQuery.service.EmployeeQueryService;
import com.example.P05_JPQLCustomQuery.service.ModifyingQueryService;
import com.example.P05_JPQLCustomQuery.service.PracticeDataService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/practice")
public class QueryPracticeController {

    private final PracticeDataService dataService;
    private final EmployeeQueryService queryService;
    private final ModifyingQueryService modifyingService;
    private final BulkQueryService bulkQueryService;

    public QueryPracticeController(
            PracticeDataService dataService,
            EmployeeQueryService queryService,
            ModifyingQueryService modifyingService,
            BulkQueryService bulkQueryService) {
        this.dataService = dataService;
        this.queryService = queryService;
        this.modifyingService = modifyingService;
        this.bulkQueryService = bulkQueryService;
    }

    // Part 6.1 + 6.9 laboratory setup
    // Creates Department data required by Employee JOIN experiments.
    @PostMapping("/departments")
    @ResponseStatus(HttpStatus.CREATED)
    public DepartmentResponse createDepartment(@Valid @RequestBody CreateDepartmentRequest request) {
        return dataService.createDepartment(request);
    }

    // Part 6.1 + 6.8 laboratory setup
    // Creates Employee data with name, email, salary, active, and departmentId.
    @PostMapping("/employees")
    @ResponseStatus(HttpStatus.CREATED)
    public EmployeeResponse createEmployee(@Valid @RequestBody CreateEmployeeRequest request) {
        return dataService.createEmployee(request);
    }

    // Part 6.1 + 6.3 + 6.4 + 6.8
    // Calls findAllEmployeesJpql(); JPQL SELECT uses Employee entity rather than employees table.
    @GetMapping("/jpql/all")
    public List<EmployeeResponse> findAllJpql() {
        return queryService.findAllJpql();
    }

    // Part 6.1 + 6.3 + 6.4 + 6.8
    // Calls findActiveEmployees(); Employee and active are entity/field names.
    @GetMapping("/jpql/active")
    public List<EmployeeResponse> findActiveJpql() {
        return queryService.findActiveJpql();
    }

    // Part 6.5
    // Calls positional JPQL where ?1=name and ?2=active.
    @GetMapping("/jpql/positional")
    public List<EmployeeResponse> findPositional(
            @RequestParam String name,
            @RequestParam boolean active) {
        return queryService.findPositional(name, active);
    }

    // Part 6.6 + 6.7
    // Calls named JPQL using :name/:active bound by @Param.
    @GetMapping("/jpql/named")
    public List<EmployeeResponse> findNamed(
            @RequestParam String name,
            @RequestParam boolean active) {
        return queryService.findNamed(name, active);
    }

    // Part 6.9
    // Calls JPQL JOIN e.department and filters on Department.name.
    @GetMapping("/jpql/join")
    public List<EmployeeResponse> findByDepartment(
            @RequestParam String departmentName) {
        return queryService.findByDepartmentName(departmentName);
    }

    // Part 6.10 + 6.11
    // Calls three JPQL aggregate queries: COUNT(e), SUM(e.salary), and AVG(e.salary).
    @GetMapping("/jpql/aggregate")
    public AggregateResult aggregate() {
        return queryService.aggregate();
    }

    // Part 6.12
    // Calls a JPQL constructor projection into EmployeeSummary.
    @GetMapping("/jpql/projection")
    public List<EmployeeSummary> employeeProjection() {
        return queryService.findEmployeeSummaries();
    }

    // Part 6.9 + 6.10 + 6.12
    // Calls LEFT JOIN/GROUP BY constructor projection into DepartmentStats.
    @GetMapping("/jpql/department-stats")
    public List<DepartmentStats> departmentStats() {
        return queryService.findDepartmentStats();
    }

    // Part 6.2 + 6.13 + 6.14 + 6.15
    // Calls real SQL against employees using @Query(nativeQuery = true).
    @GetMapping("/native/active")
    public List<EmployeeResponse> findActiveNative() {
        return queryService.findActiveNative();
    }

    // Part 6.16 + 6.17 + 6.18
    // Calls JPQL UPDATE through @Modifying and returns the affected-row count.
    @PatchMapping("/modify/{id}/deactivate")
    public AffectedRowsResult deactivateEmployee(@PathVariable Long id) {
        return modifyingService.deactivateEmployee(id);
    }

    // Part 6.16 + 6.17 + 6.18
    // Calls JPQL UPDATE salary through @Modifying and returns the affected-row count.
    @PatchMapping("/modify/{id}/salary")
    public AffectedRowsResult updateSalary(
            @PathVariable Long id,
            @Valid @RequestBody UpdateSalaryRequest request) {
        return modifyingService.updateSalary(id, request.salary());
    }

    // Part 6.16 + 6.17 + 6.19
    // Calls JPQL DELETE through @Modifying and returns the affected-row count.
    @DeleteMapping("/modify/inactive")
    public AffectedRowsResult deleteInactiveEmployees() {
        return modifyingService.deleteInactiveEmployees();
    }

    // Part 6.20 - Experiment A
    // Bulk UPDATE leaves the managed Employee stale; service then clears and reloads it.
    @PostMapping("/bulk/stale-context/{id}")
    public BulkUpdateResult demonstrateStaleContext(
            @PathVariable Long id,
            @Valid @RequestBody UpdateNameRequest request) {
        return bulkQueryService.demonstrateStaleContext(id, request.name());
    }

    // Part 6.20 - Experiment B
    // @Modifying(clearAutomatically = true) detaches the old object before service reloads it.
    @PostMapping("/bulk/auto-clear/{id}")
    public AutoClearBulkUpdateResult demonstrateAutomaticClear(
            @PathVariable Long id,
            @Valid @RequestBody UpdateNameRequest request) {
        return bulkQueryService.demonstrateAutomaticClear(id, request.name());
    }
}
