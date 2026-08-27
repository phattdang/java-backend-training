package com.example.P04_EntityRelationships.studentcourse.controller;

import com.example.P04_EntityRelationships.studentcourse.dto.CourseResponse;
import com.example.P04_EntityRelationships.studentcourse.dto.CreateCourseRequest;
import com.example.P04_EntityRelationships.studentcourse.dto.CreateStudentRequest;
import com.example.P04_EntityRelationships.studentcourse.dto.StudentResponse;
import com.example.P04_EntityRelationships.studentcourse.service.StudentCourseService;
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
public class StudentCourseController {

    private final StudentCourseService service;

    public StudentCourseController(StudentCourseService service) {
        this.service = service;
    }

    // Part 7.1 + 7.8
    // Creates the owning Student used by the direct N-N Course scenario.
    @PostMapping("/students")
    @ResponseStatus(HttpStatus.CREATED)
    public StudentResponse createStudent(@Valid @RequestBody CreateStudentRequest request) {
        return service.createStudent(request);
    }

    // Part 7.1 + 7.8
    // Creates the inverse Course used by the direct N-N Student scenario.
    @PostMapping("/courses")
    @ResponseStatus(HttpStatus.CREATED)
    public CourseResponse createCourse(@Valid @RequestBody CreateCourseRequest request) {
        return service.createCourse(request);
    }

    // Part 7.3 + 7.4 + 7.8 + 7.11 + 7.14 + 7.16 + 7.17
    // Student owns @JoinTable; addCourse() synchronizes both Java sets and writes student_courses.
    @PostMapping("/students/{studentId}/courses/{courseId}")
    public StudentResponse addCourse(
            @PathVariable Long studentId,
            @PathVariable Long courseId) {
        return service.addCourse(studentId, courseId);
    }

    // Part 7.8 + 7.10 + 7.11 + 7.16
    // Reads Student.courses and returns only course IDs to avoid recursive JSON serialization.
    @GetMapping("/students/{studentId}")
    public StudentResponse findStudentWithCourses(@PathVariable Long studentId) {
        return service.findStudentWithCourses(studentId);
    }
}
