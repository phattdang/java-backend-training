package com.example.P05_JPQLCustomQuery.repository;

import com.example.P05_JPQLCustomQuery.dto.DepartmentStats;
import com.example.P05_JPQLCustomQuery.dto.EmployeeSummary;
import com.example.P05_JPQLCustomQuery.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.util.List;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {

    // Employee is the entity name; active is a Java field, not a table/column reference.
    @Query("""
            SELECT e
            FROM Employee e
            WHERE e.active = true
            """)
    List<Employee> findActiveEmployees();

    @Query("""
            SELECT e
            FROM Employee e
            """)
    List<Employee> findAllEmployeesJpql();

    // ?1 is the first Java parameter; ?2 is the second Java parameter.
    @Query("""
            SELECT e
            FROM Employee e
            WHERE e.name = ?1
              AND e.active = ?2
            """)
    List<Employee> findByNameAndActivePositional(String name, boolean active);

    @Query("""
            SELECT e
            FROM Employee e
            WHERE e.name = :name
              AND e.active = :active
            """)
    List<Employee> findByNameAndActiveNamed(
            @Param("name") String name,
            @Param("active") boolean active);

    // JOIN follows the Java relationship e.department, not a raw SQL ON condition.
    @Query("""
            SELECT e
            FROM Employee e
            JOIN e.department d
            WHERE d.name = :departmentName
            """)
    List<Employee> findEmployeesByDepartmentName(
            @Param("departmentName") String departmentName);

    @Query("SELECT COUNT(e) FROM Employee e")
    long countEmployeesJpql();

    @Query("SELECT SUM(e.salary) FROM Employee e")
    BigDecimal sumEmployeeSalary();

    @Query("SELECT AVG(e.salary) FROM Employee e")
    Double averageEmployeeSalary();

    // The constructor name below is the actual project package, not a placeholder.
    @Query("""
            SELECT new com.example.P05_JPQLCustomQuery.dto.EmployeeSummary(
                e.id,
                e.name,
                e.email
            )
            FROM Employee e
            """)
    List<EmployeeSummary> findEmployeeSummaries();

    @Query("""
            SELECT new com.example.P05_JPQLCustomQuery.dto.DepartmentStats(
                d.name,
                COUNT(e)
            )
            FROM Department d
            LEFT JOIN d.employees e
            GROUP BY d.id, d.name
            ORDER BY d.name
            """)
    List<DepartmentStats> findDepartmentStats();

    // This is physical PostgreSQL SQL: employees is a table and active is a column.
    @Query(
            value = """
                    SELECT *
                    FROM employees
                    WHERE active = true
                    """,
            nativeQuery = true
    )
    List<Employee> findActiveEmployeesNative();

    @Modifying
    @Query("""
            UPDATE Employee e
            SET e.active = false
            WHERE e.id = :id
            """)
    int deactivateEmployee(@Param("id") Long id);

    @Modifying
    @Query("""
            UPDATE Employee e
            SET e.salary = :salary
            WHERE e.id = :id
            """)
    int updateSalary(@Param("id") Long id, @Param("salary") BigDecimal salary);

    @Modifying
    @Query("""
            DELETE FROM Employee e
            WHERE e.active = false
            """)
    int deleteInactiveEmployees();

    // Experiment A: deliberately leaves the Persistence Context unchanged.
    @Modifying
    @Query("""
            UPDATE Employee e
            SET e.name = :name
            WHERE e.id = :id
            """)
    int bulkUpdateName(@Param("id") Long id, @Param("name") String name);

    // Experiment B: Spring Data clears the Persistence Context after executeUpdate().
    @Modifying(clearAutomatically = true)
    @Query("""
            UPDATE Employee e
            SET e.name = :name
            WHERE e.id = :id
            """)
    int bulkUpdateNameAndClear(@Param("id") Long id, @Param("name") String name);
}
