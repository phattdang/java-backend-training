package com.example.P04_EntityRelationships.enrollment.service;

import com.example.P04_EntityRelationships.enrollment.dto.CreateEnrollmentCourseRequest;
import com.example.P04_EntityRelationships.enrollment.dto.CreateEnrollmentRequest;
import com.example.P04_EntityRelationships.enrollment.dto.CreateEnrollmentStudentRequest;
import com.example.P04_EntityRelationships.enrollment.dto.EnrollmentPartyResponse;
import com.example.P04_EntityRelationships.enrollment.dto.EnrollmentResponse;
import com.example.P04_EntityRelationships.enrollment.entity.Enrollment;
import com.example.P04_EntityRelationships.enrollment.entity.EnrollmentCourse;
import com.example.P04_EntityRelationships.enrollment.entity.EnrollmentStudent;
import com.example.P04_EntityRelationships.enrollment.repository.EnrollmentCourseRepository;
import com.example.P04_EntityRelationships.enrollment.repository.EnrollmentRepository;
import com.example.P04_EntityRelationships.enrollment.repository.EnrollmentStudentRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;

@Service
public class EnrollmentService {

    private final EnrollmentStudentRepository studentRepository;
    private final EnrollmentCourseRepository courseRepository;
    private final EnrollmentRepository enrollmentRepository;

    public EnrollmentService(
            EnrollmentStudentRepository studentRepository,
            EnrollmentCourseRepository courseRepository,
            EnrollmentRepository enrollmentRepository) {
        this.studentRepository = studentRepository;
        this.courseRepository = courseRepository;
        this.enrollmentRepository = enrollmentRepository;
    }

    public EnrollmentPartyResponse createStudent(CreateEnrollmentStudentRequest request) {
        EnrollmentStudent student = studentRepository.save(new EnrollmentStudent(request.name()));
        return new EnrollmentPartyResponse(student.getId(), student.getName());
    }

    public EnrollmentPartyResponse createCourse(CreateEnrollmentCourseRequest request) {
        EnrollmentCourse course = courseRepository.save(new EnrollmentCourse(request.title()));
        return new EnrollmentPartyResponse(course.getId(), course.getTitle());
    }

    public EnrollmentResponse createEnrollment(CreateEnrollmentRequest request) {
        EnrollmentStudent student = studentRepository.findById(request.studentId())
                .orElseThrow(() -> notFound("EnrollmentStudent", request.studentId()));
        EnrollmentCourse course = courseRepository.findById(request.courseId())
                .orElseThrow(() -> notFound("EnrollmentCourse", request.courseId()));
        LocalDateTime enrolledAt = request.enrolledAt() == null ? LocalDateTime.now() : request.enrolledAt();

        Enrollment enrollment = new Enrollment(
                student,
                course,
                enrolledAt,
                request.status(),
                request.grade()
        );
        return toResponse(enrollmentRepository.save(enrollment));
    }

    public EnrollmentResponse findEnrollment(Long enrollmentId) {
        Enrollment enrollment = enrollmentRepository.findById(enrollmentId)
                .orElseThrow(() -> notFound("Enrollment", enrollmentId));
        return toResponse(enrollment);
    }

    private EnrollmentResponse toResponse(Enrollment enrollment) {
        return new EnrollmentResponse(
                enrollment.getId(),
                enrollment.getStudent().getId(),
                enrollment.getCourse().getId(),
                enrollment.getEnrolledAt(),
                enrollment.getStatus(),
                enrollment.getGrade()
        );
    }

    private ResponseStatusException notFound(String type, Long id) {
        return new ResponseStatusException(HttpStatus.NOT_FOUND, type + " not found: " + id);
    }
}
