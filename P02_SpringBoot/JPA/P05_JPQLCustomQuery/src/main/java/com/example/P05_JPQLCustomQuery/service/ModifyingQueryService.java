package com.example.P05_JPQLCustomQuery.service;

import com.example.P05_JPQLCustomQuery.dto.AffectedRowsResult;
import com.example.P05_JPQLCustomQuery.repository.EmployeeRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
public class ModifyingQueryService {

    private final EmployeeRepository employeeRepository;

    public ModifyingQueryService(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    @Transactional
    public AffectedRowsResult deactivateEmployee(Long id) {
        int affectedRows = employeeRepository.deactivateEmployee(id);
        return new AffectedRowsResult("JPQL bulk UPDATE active=false", affectedRows);
    }

    @Transactional
    public AffectedRowsResult updateSalary(Long id, BigDecimal salary) {
        int affectedRows = employeeRepository.updateSalary(id, salary);
        return new AffectedRowsResult("JPQL bulk UPDATE salary", affectedRows);
    }

    @Transactional
    public AffectedRowsResult deleteInactiveEmployees() {
        int affectedRows = employeeRepository.deleteInactiveEmployees();
        return new AffectedRowsResult("JPQL bulk DELETE inactive employees", affectedRows);
    }
}
