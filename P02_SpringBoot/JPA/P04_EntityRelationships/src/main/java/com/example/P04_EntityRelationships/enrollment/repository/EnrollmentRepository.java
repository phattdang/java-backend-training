package com.example.P04_EntityRelationships.enrollment.repository;

import com.example.P04_EntityRelationships.enrollment.entity.Enrollment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EnrollmentRepository extends JpaRepository<Enrollment, Long> {
}
