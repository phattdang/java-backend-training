package com.example.P06_FetchingLazyEager.eagerdemo.repository;

import com.example.P06_FetchingLazyEager.eagerdemo.entity.EagerEmployee;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EagerEmployeeRepository extends JpaRepository<EagerEmployee, Long> {
}
