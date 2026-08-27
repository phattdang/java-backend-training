package com.example.P06_FetchingLazyEager.service;

import com.example.P06_FetchingLazyEager.dto.LazyExceptionResult;
import com.example.P06_FetchingLazyEager.entity.Employee;
import lombok.RequiredArgsConstructor;
import org.hibernate.LazyInitializationException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LazyInitializationExceptionService {

    private final DetachedEmployeeLoader detachedEmployeeLoader;

    // Intentionally non-transactional: the loader's Persistence Context has ended here.
    public LazyExceptionResult demonstrate(Long employeeId) {
        Employee detachedEmployee = detachedEmployeeLoader.loadEmployeeWithoutDepartment(employeeId);

        try {
            String unexpectedValue = detachedEmployee.getDepartment().getName();
            return new LazyExceptionResult(
                    false,
                    "NONE",
                    "Unexpectedly loaded department: " + unexpectedValue,
                    "The relation was already initialized, so no exception occurred.");
        } catch (LazyInitializationException exception) {
            return new LazyExceptionResult(
                    true,
                    exception.getClass().getSimpleName(),
                    exception.getMessage(),
                    "Department was accessed after its Persistence Context had closed; OSIV is disabled.");
        }
    }
}
