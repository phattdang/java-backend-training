package com.example.P04_EntityRelationships.studentcourse.repository;

import com.example.P04_EntityRelationships.studentcourse.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StudentRepository extends JpaRepository<Student, Long> {
}
