package com.example.P04_EntityRelationships.studentcourse.repository;

import com.example.P04_EntityRelationships.studentcourse.entity.Course;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CourseRepository extends JpaRepository<Course, Long> {
}
