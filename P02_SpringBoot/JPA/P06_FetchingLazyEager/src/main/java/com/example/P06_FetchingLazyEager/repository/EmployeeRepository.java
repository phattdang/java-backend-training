package com.example.P06_FetchingLazyEager.repository;

import com.example.P06_FetchingLazyEager.entity.Employee;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {

    @Query("""
            SELECT e
            FROM Employee e
            JOIN FETCH e.department
            WHERE e.id = :id
            """)
    Optional<Employee> findByIdWithDepartment(@Param("id") Long id);

    @EntityGraph(attributePaths = "department")
    @Query("""
            SELECT e
            FROM Employee e
            WHERE e.id = :id
            """)
    Optional<Employee> findByIdWithDepartmentEntityGraph(@Param("id") Long id);
}
