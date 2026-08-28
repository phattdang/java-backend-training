package com.example.P08_NPlusOnePractice.repository;

import com.example.P08_NPlusOnePractice.entity.Department;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface DepartmentRepository extends JpaRepository<Department, Long> {

    @Query("""
            SELECT DISTINCT d
            FROM Department d
            LEFT JOIN FETCH d.employees
            """)
    List<Department> findAllWithEmployeesFetchJoin();

    @EntityGraph(attributePaths = "employees")
    @Query("SELECT d FROM Department d")
    List<Department> findAllWithEmployeesEntityGraph();
}
