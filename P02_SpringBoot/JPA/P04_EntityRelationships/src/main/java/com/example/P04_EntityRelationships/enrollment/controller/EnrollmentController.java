package com.example.P04_EntityRelationships.enrollment.controller;

import com.example.P04_EntityRelationships.enrollment.dto.CreateEnrollmentCourseRequest;
import com.example.P04_EntityRelationships.enrollment.dto.CreateEnrollmentRequest;
import com.example.P04_EntityRelationships.enrollment.dto.CreateEnrollmentStudentRequest;
import com.example.P04_EntityRelationships.enrollment.dto.EnrollmentPartyResponse;
import com.example.P04_EntityRelationships.enrollment.dto.EnrollmentResponse;
import com.example.P04_EntityRelationships.enrollment.service.EnrollmentService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/practice")
public class EnrollmentController {

    private final EnrollmentService service;

    public EnrollmentController(EnrollmentService service) {
        this.service = service;
    }

    // Part 7.12 + 7.19
    // Creates a student for the isolated intermediate-entity scenario; it has no back-reference collection.
    @PostMapping("/enrollment-students")
    @ResponseStatus(HttpStatus.CREATED)
    public EnrollmentPartyResponse createStudent(
            @Valid @RequestBody CreateEnrollmentStudentRequest request) {
        return service.createStudent(request);
    }

    // Part 7.12 + 7.19
    // Creates a course for the isolated intermediate-entity scenario; it has no back-reference collection.
    @PostMapping("/enrollment-courses")
    @ResponseStatus(HttpStatus.CREATED)
    public EnrollmentPartyResponse createCourse(
            @Valid @RequestBody CreateEnrollmentCourseRequest request) {
        return service.createCourse(request);
    }

    // Part 7.2 + 7.7 + 7.9 + 7.12 + 7.18-7.20
    // Creates Enrollment with student_id, course_id, enrolledAt, status, and grade instead of direct ManyToMany.
    @PostMapping("/enrollments")
    @ResponseStatus(HttpStatus.CREATED)
    public EnrollmentResponse createEnrollment(@Valid @RequestBody CreateEnrollmentRequest request) {
        return service.createEnrollment(request);
    }

    // Part 7.12 + 7.19 + 7.20
    // Reads the unidirectional Enrollment-to-Student/Course mappings as a non-recursive DTO.
    @GetMapping("/enrollments/{enrollmentId}")
    public EnrollmentResponse findEnrollment(@PathVariable Long enrollmentId) {
        return service.findEnrollment(enrollmentId);
    }
}
