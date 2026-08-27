package com.example.P04_EntityRelationships.enrollment.repository;

import com.example.P04_EntityRelationships.enrollment.entity.EnrollmentStudent;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EnrollmentStudentRepository extends JpaRepository<EnrollmentStudent, Long> {
}
