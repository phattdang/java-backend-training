package com.example.P04_EntityRelationships.enrollment.repository;

import com.example.P04_EntityRelationships.enrollment.entity.EnrollmentCourse;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EnrollmentCourseRepository extends JpaRepository<EnrollmentCourse, Long> {
}
