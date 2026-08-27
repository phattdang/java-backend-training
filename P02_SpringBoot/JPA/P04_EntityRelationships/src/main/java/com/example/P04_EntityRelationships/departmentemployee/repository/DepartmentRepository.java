package com.example.P04_EntityRelationships.departmentemployee.repository;

import com.example.P04_EntityRelationships.departmentemployee.entity.Department;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DepartmentRepository extends JpaRepository<Department, Long> {
}
