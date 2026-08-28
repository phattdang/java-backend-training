package com.example.P08_NPlusOnePractice.repository;

import com.example.P08_NPlusOnePractice.dto.EmployeeDepartmentView;
import com.example.P08_NPlusOnePractice.entity.Employee;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {

    @Query("""
            SELECT e
            FROM Employee e
            JOIN FETCH e.department
            """)
    List<Employee> findAllWithDepartmentFetchJoin();

    @EntityGraph(attributePaths = "department")
    @Query("SELECT e FROM Employee e")
    List<Employee> findAllWithDepartmentEntityGraph();

    @Query("""
            SELECT new com.example.P08_NPlusOnePractice.dto.EmployeeDepartmentView(
                e.id,
                e.name,
                d.name
            )
            FROM Employee e
            JOIN e.department d
            """)
    List<EmployeeDepartmentView> findEmployeeDepartmentViews();
}
