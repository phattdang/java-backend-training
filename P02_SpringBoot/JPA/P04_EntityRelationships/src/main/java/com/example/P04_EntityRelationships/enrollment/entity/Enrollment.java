package com.example.P04_EntityRelationships.enrollment.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "enrollments")
@Getter
@Setter
@NoArgsConstructor
public class Enrollment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Unidirectional relationships: Enrollment can navigate to both entities,
    // while EnrollmentStudent and EnrollmentCourse have no collection back to Enrollment.
    @ManyToOne
    @JoinColumn(name = "student_id", nullable = false)
    private EnrollmentStudent student;

    @ManyToOne
    @JoinColumn(name = "course_id", nullable = false)
    private EnrollmentCourse course;

    @Column(name = "enrolled_at", nullable = false)
    private LocalDateTime enrolledAt;

    @Column(nullable = false, length = 30)
    private String status;

    @Column(length = 10)
    private String grade;

    public Enrollment(
            EnrollmentStudent student,
            EnrollmentCourse course,
            LocalDateTime enrolledAt,
            String status,
            String grade) {
        this.student = student;
        this.course = course;
        this.enrolledAt = enrolledAt;
        this.status = status;
        this.grade = grade;
    }
}
