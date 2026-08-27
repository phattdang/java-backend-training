package com.example.P05_JPQLCustomQuery.repository;

import com.example.P05_JPQLCustomQuery.entity.Department;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DepartmentRepository extends JpaRepository<Department, Long> {
}
