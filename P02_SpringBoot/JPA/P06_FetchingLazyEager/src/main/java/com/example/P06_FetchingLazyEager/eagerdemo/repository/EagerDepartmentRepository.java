package com.example.P06_FetchingLazyEager.eagerdemo.repository;

import com.example.P06_FetchingLazyEager.eagerdemo.entity.EagerDepartment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EagerDepartmentRepository extends JpaRepository<EagerDepartment, Long> {
}
