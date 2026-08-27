package com.example.P04_EntityRelationships.departmentemployee.repository;

import com.example.P04_EntityRelationships.departmentemployee.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {
}
