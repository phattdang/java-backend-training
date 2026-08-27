package com.example.P04_EntityRelationships.studentcourse.service;

import com.example.P04_EntityRelationships.studentcourse.dto.CourseResponse;
import com.example.P04_EntityRelationships.studentcourse.dto.CreateCourseRequest;
import com.example.P04_EntityRelationships.studentcourse.dto.CreateStudentRequest;
import com.example.P04_EntityRelationships.studentcourse.dto.StudentResponse;
import com.example.P04_EntityRelationships.studentcourse.entity.Course;
import com.example.P04_EntityRelationships.studentcourse.entity.Student;
import com.example.P04_EntityRelationships.studentcourse.repository.CourseRepository;
import com.example.P04_EntityRelationships.studentcourse.repository.StudentRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.Set;
import java.util.stream.Collectors;

@Service
public class StudentCourseService {

    private final StudentRepository studentRepository;
    private final CourseRepository courseRepository;

    public StudentCourseService(StudentRepository studentRepository, CourseRepository courseRepository) {
        this.studentRepository = studentRepository;
        this.courseRepository = courseRepository;
    }

    public StudentResponse createStudent(CreateStudentRequest request) {
        Student student = studentRepository.save(new Student(request.name()));
        return new StudentResponse(student.getId(), student.getName(), Set.of());
    }

    public CourseResponse createCourse(CreateCourseRequest request) {
        Course course = courseRepository.save(new Course(request.title()));
        return new CourseResponse(course.getId(), course.getTitle());
    }

    @Transactional
    public StudentResponse addCourse(Long studentId, Long courseId) {
        Student student = findStudent(studentId);
        Course course = findCourse(courseId);

        student.addCourse(course);
        // Student owns @JoinTable, so persisting its collection controls student_courses.
        studentRepository.save(student);
        return toStudentResponse(student);
    }

    @Transactional(readOnly = true)
    public StudentResponse findStudentWithCourses(Long studentId) {
        return toStudentResponse(findStudent(studentId));
    }

    private Student findStudent(Long id) {
        return studentRepository.findById(id)
                .orElseThrow(() -> notFound("Student", id));
    }

    private Course findCourse(Long id) {
        return courseRepository.findById(id)
                .orElseThrow(() -> notFound("Course", id));
    }

    private StudentResponse toStudentResponse(Student student) {
        Set<Long> courseIds = student.getCourses().stream()
                .map(Course::getId)
                .collect(Collectors.toSet());
        return new StudentResponse(student.getId(), student.getName(), courseIds);
    }

    private ResponseStatusException notFound(String type, Long id) {
        return new ResponseStatusException(HttpStatus.NOT_FOUND, type + " not found: " + id);
    }
}
