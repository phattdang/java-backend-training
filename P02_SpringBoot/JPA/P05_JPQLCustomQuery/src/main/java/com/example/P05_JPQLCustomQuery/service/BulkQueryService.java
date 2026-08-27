package com.example.P05_JPQLCustomQuery.service;

import com.example.P05_JPQLCustomQuery.dto.AutoClearBulkUpdateResult;
import com.example.P05_JPQLCustomQuery.dto.BulkUpdateResult;
import com.example.P05_JPQLCustomQuery.entity.Employee;
import com.example.P05_JPQLCustomQuery.repository.EmployeeRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
public class BulkQueryService {

    private final EmployeeRepository employeeRepository;

    @PersistenceContext
    private EntityManager entityManager;

    public BulkQueryService(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    @Transactional
    public BulkUpdateResult demonstrateStaleContext(Long employeeId, String newName) {
        Employee managedEmployee = findManagedEmployee(employeeId);
        String managedValueBefore = managedEmployee.getName();
        boolean managedBeforeClear = entityManager.contains(managedEmployee);

        // Bulk JPQL goes straight to the database and does not mutate managedEmployee.
        int affectedRows = employeeRepository.bulkUpdateName(employeeId, newName);
        String databaseValueAfter = readNameDirectlyFromDatabase(employeeId);
        String managedValueAfter = managedEmployee.getName();

        entityManager.clear();
        boolean managedAfterClear = entityManager.contains(managedEmployee);
        Employee reloadedEmployee = findManagedEmployee(employeeId);

        return new BulkUpdateResult(
                affectedRows,
                managedValueBefore,
                databaseValueAfter,
                managedValueAfter,
                managedBeforeClear,
                managedAfterClear,
                reloadedEmployee.getName()
        );
    }

    @Transactional
    public AutoClearBulkUpdateResult demonstrateAutomaticClear(Long employeeId, String newName) {
        Employee employeeBeforeQuery = findManagedEmployee(employeeId);
        String valueBefore = employeeBeforeQuery.getName();
        boolean managedBeforeQuery = entityManager.contains(employeeBeforeQuery);

        int affectedRows = employeeRepository.bulkUpdateNameAndClear(employeeId, newName);
        boolean managedAfterQuery = entityManager.contains(employeeBeforeQuery);
        String oldObjectValueAfter = employeeBeforeQuery.getName();
        Employee reloadedEmployee = findManagedEmployee(employeeId);

        return new AutoClearBulkUpdateResult(
                affectedRows,
                valueBefore,
                oldObjectValueAfter,
                managedBeforeQuery,
                managedAfterQuery,
                reloadedEmployee.getName()
        );
    }

    private Employee findManagedEmployee(Long employeeId) {
        Employee employee = entityManager.find(Employee.class, employeeId);
        if (employee == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Employee not found: " + employeeId);
        }
        return employee;
    }

    private String readNameDirectlyFromDatabase(Long employeeId) {
        Object value = entityManager.createNativeQuery(
                        "SELECT name FROM employees WHERE id = :id")
                .setParameter("id", employeeId)
                .getSingleResult();
        return value.toString();
    }
}
