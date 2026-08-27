package com.example.P06_FetchingLazyEager.repository;

import com.example.P06_FetchingLazyEager.entity.Department;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DepartmentRepository extends JpaRepository<Department, Long> {
}
