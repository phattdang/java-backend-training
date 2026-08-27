package com.example.P06_FetchingLazyEager.service;

import com.example.P06_FetchingLazyEager.dto.EmployeeDetailResponse;
import com.example.P06_FetchingLazyEager.entity.Employee;
import com.example.P06_FetchingLazyEager.repository.EmployeeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.Hibernate;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
@Slf4j
public class FetchJoinService {

    private final EmployeeRepository employeeRepository;

    @Transactional(readOnly = true)
    public EmployeeDetailResponse findEmployeeDetail(Long id) {
        Employee employee = employeeRepository.findByIdWithDepartment(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Employee not found: " + id));
        log.info("JOIN FETCH result - Department initialized? {}",
                Hibernate.isInitialized(employee.getDepartment()));
        return toDetail(employee);
    }

    private EmployeeDetailResponse toDetail(Employee employee) {
        return new EmployeeDetailResponse(
                employee.getId(),
                employee.getName(),
                employee.getEmail(),
                employee.getSalary(),
                employee.getDepartment().getId(),
                employee.getDepartment().getName(),
                employee.getDepartment().getDescription());
    }
}
