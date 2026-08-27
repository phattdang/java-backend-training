# Part 7 - Entity Relationships Guide

## Checklist map

| Checklist | Concept | File | Field / Method / Endpoint |
| --- | --- | --- | --- |
| 7.1 | Entity relationship | `Department.java`, `Employee.java` | `employees`, `department` |
| 7.2 | Foreign Key | `Employee.java`, `Profile.java`, `Enrollment.java` | `department`, `user`, `student`, `course` |
| 7.3 | Owning Side | `Employee.java`, `Profile.java`, `Student.java` | `department`, `user`, `courses` |
| 7.4 | Inverse Side | `Department.java`, `User.java`, `Course.java` | `employees`, `profile`, `students` |
| 7.5 | `@OneToOne` | `Profile.java`, `User.java` | `user`, `profile` |
| 7.6 | `@OneToMany` | `Department.java` | `employees` |
| 7.7 | `@ManyToOne` | `Employee.java`, `Enrollment.java` | `department`, `student`, `course` |
| 7.8 | `@ManyToMany` | `Student.java`, `Course.java` | `courses`, `students` |
| 7.9 | `@JoinColumn` | `Employee.java`, `Profile.java`, `Enrollment.java` | relationship fields |
| 7.10 | `mappedBy` | `Department.java`, `User.java`, `Course.java` | inverse relationship fields |
| 7.11 | Bidirectional relationship | three bidirectional scenarios | relationship fields and helper methods |
| 7.12 | Unidirectional relationship | `Enrollment.java` | `student`, `course` |
| 7.13 | Side holding the FK | `Employee.java`, `Profile.java`, `Enrollment.java` | owning-side fields |
| 7.14 | Synchronizing both sides | `Department.java`, `Student.java`, `User.java` | helper methods |
| 7.15 | Helper methods | `Department.java`, `Student.java`, `User.java` | `addEmployee()`, `removeEmployee()`, `addCourse()`, `removeCourse()`, `attachProfile()` |
| 7.16 | Join Table | `Student.java` | `student_courses` |
| 7.17 | `@JoinTable` | `Student.java` | `courses` |
| 7.18 | When to avoid `@ManyToMany` | this guide, `EnrollmentController.java` | `POST /practice/enrollments` |
| 7.19 | Intermediate Entity | `Enrollment.java` | entity declaration and business fields |
| 7.20 | Mapping according to schema | all four scenario entity packages | table and join annotations |

Entity paths in the table are relative to:

```text
src/main/java/com/example/P04_EntityRelationships/<scenario>/entity/
```

## 7.1 Entity relationship concept

**Files:**

- `departmentemployee/entity/Department.java`
- `departmentemployee/entity/Employee.java`

**Fields:** `Department.employees`, `Employee.department`

**Relevant code:**

```java
@OneToMany(mappedBy = "department")
private List<Employee> employees;

@ManyToOne
@JoinColumn(name = "department_id")
private Department department;
```

**What it demonstrates:** Java object references represent the database relationship between rows. An annotation mapping must agree with the real FK/table design.

**How to verify:** Create a department and employee, then inspect `employees.department_id`.

## 7.2 Foreign Key

**Files:** `Employee.java`, `Profile.java`, `Enrollment.java`

**Fields:** `department`, `user`, `student`, `course`

**Relevant code:**

```java
@JoinColumn(name = "department_id")
@JoinColumn(name = "user_id", unique = true)
@JoinColumn(name = "student_id")
@JoinColumn(name = "course_id")
```

**What it demonstrates:** Each `@JoinColumn` maps an entity reference to a concrete FK column.

**How to verify:** Inspect FK constraints on `employees`, `profiles`, and `enrollments` in DBeaver.

## 7.3 Owning Side

**Files:** `Employee.java`, `Profile.java`, `Student.java`

**Fields:** `department`, `user`, `courses`

**Relevant code:**

```java
employee.setDepartment(department);
profile.setUser(user);
student.getCourses().add(course);
```

**Why these are owning sides:** `Employee` maps `department_id`, `Profile` maps `user_id`, and `Student` declares `@JoinTable`. Changes on these sides control the relationship SQL.

**How to verify:** Call the relationship-creation APIs and inspect INSERT/UPDATE statements.

## 7.4 Inverse Side

**Files:** `Department.java`, `User.java`, `Course.java`

**Fields:** `employees`, `profile`, `students`

**Relevant code:**

```java
@OneToMany(mappedBy = "department")
@OneToOne(mappedBy = "user")
@ManyToMany(mappedBy = "courses")
```

**What it demonstrates:** The inverse side mirrors navigation in Java but does not define the FK or join table.

**How to verify:** The generated schema contains only one FK/join representation for each relationship, not one per Java side.

## 7.5 @OneToOne

**Files:**

- `userprofile/entity/Profile.java`
- `userprofile/entity/User.java`

**Fields:** `Profile.user`, `User.profile`

**Relevant code:**

```java
@OneToOne
@JoinColumn(name = "user_id", nullable = false, unique = true)
private User user;
```

**What it demonstrates:** `profiles.user_id` is a unique FK, so one user can be associated with at most one profile. `Profile` owns the relationship; `User` is inverse.

**How to verify:** Call `POST /practice/users/{userId}/profile` and inspect the unique constraint on `profiles.user_id`.

## 7.6 @OneToMany

**File:** `departmentemployee/entity/Department.java`

**Field:** `employees`

**Relevant code:**

```java
@OneToMany(mappedBy = "department")
private List<Employee> employees;
```

**What it demonstrates:** One department can be referenced by multiple employee rows. The list is inverse and does not create a second FK.

**How to verify:** Add multiple employees to one department, then call `GET /practice/departments/{departmentId}`.

## 7.7 @ManyToOne

**Files:** `departmentemployee/entity/Employee.java`, `enrollment/entity/Enrollment.java`

**Fields:** `department`, `student`, `course`

**Relevant code:**

```java
@ManyToOne
@JoinColumn(name = "department_id")
private Department department;
```

**What it demonstrates:** Many rows can point to one parent. In the common One-to-Many/Many-to-One design, the Many side stores the FK.

**How to verify:** Inspect `employees.department_id`, `enrollments.student_id`, and `enrollments.course_id`.

## 7.8 @ManyToMany

**Files:** `studentcourse/entity/Student.java`, `studentcourse/entity/Course.java`

**Fields:** `Student.courses`, `Course.students`

**Relevant code:**

```java
@ManyToMany
@JoinTable(name = "student_courses", ...)
private Set<Course> courses;
```

**What it demonstrates:** Students and courses are linked through the separate `student_courses` table.

**How to verify:** Create a student and course, call `POST /practice/students/{studentId}/courses/{courseId}`, then inspect `student_courses`.

## 7.9 @JoinColumn

**Files:** `Employee.java`, `Profile.java`, `Enrollment.java`

**Fields:** all owning `@ManyToOne` and `@OneToOne` references

**Relevant code:**

```java
@JoinColumn(name = "department_id")
private Department department;
```

**What it demonstrates:** `@JoinColumn` gives the Java relationship field a concrete FK column in the owning table.

**How to verify:** Compare annotation names with the generated column names in PostgreSQL.

## 7.10 mappedBy

**Files:** `Department.java`, `User.java`, `Course.java`

**Fields:** `employees`, `profile`, `students`

**Relevant code:**

```java
@OneToMany(mappedBy = "department")
```

**What it demonstrates:** `mappedBy = "department"` points to the Java field `Employee.department`. It does not point to the database column `department_id`.

**How to verify:** Search for the named Java fields on each owning entity and compare them with `mappedBy`.

## 7.11 Bidirectional relationship

**Scenarios:** Department/Employee, User/Profile, Student/Course

**Relevant navigation:**

```text
Department.employees <-> Employee.department
User.profile         <-> Profile.user
Student.courses      <-> Course.students
```

**What it demonstrates:** Both Java entities can navigate the association. Bidirectional does not mean that the database has two FKs.

**How to verify:** Inspect the DTO read endpoints and compare them with the single FK/join-table representation.

## 7.12 Unidirectional relationship

**File:** `enrollment/entity/Enrollment.java`

**Fields:** `student`, `course`

**Relevant code:**

```java
private EnrollmentStudent student;
private EnrollmentCourse course;
```

**What it demonstrates:** `Enrollment` can navigate to student and course, while those two entity classes contain no back-reference to enrollments. Unidirectional mappings are simpler when reverse navigation is unnecessary.

**How to verify:** Open `EnrollmentStudent.java` and `EnrollmentCourse.java`; neither declares a relationship collection.

## 7.13 Which side holds the Foreign Key

**Files and fields:**

```text
Employee.department -> employees.department_id
Profile.user         -> profiles.user_id
Enrollment.student   -> enrollments.student_id
Enrollment.course    -> enrollments.course_id
Student.courses      -> student_courses join-table rows
```

**What it demonstrates:** The side mapping the FK or `@JoinTable` is the owning side. The Many side usually holds the FK in One-to-Many/Many-to-One.

**How to verify:** Compare owning annotations with DBeaver's constraint view.

## 7.14 Synchronizing both sides

**Files:** `Department.java`, `User.java`, `Student.java`

**Methods:** `addEmployee()`, `removeEmployee()`, `attachProfile()`, `addCourse()`, `removeCourse()`

**Relevant code:**

```java
employees.add(employee);
employee.setDepartment(this);
```

**What it demonstrates:** A helper updates both in-memory references so the Java object graph is internally consistent.

**How to verify:** Call `POST /practice/departments/{departmentId}/employees/helper`; the service explicitly saves the owning Employee afterward.

## 7.15 Helper methods

**File:** `departmentemployee/entity/Department.java`

**Methods:** `addEmployee(...)`, `removeEmployee(...)`

**Relevant code:**

```java
public void removeEmployee(Employee employee) {
    employees.remove(employee);
    employee.setDepartment(null);
}
```

**What it demonstrates:** Helper methods do not save to the database. They synchronize Java references; persistence still depends on the owning side and normal repository/dirty-checking behavior. No cascade is configured in this project.

**How to verify:** Call the helper create and helper remove endpoints and observe the explicit Employee save plus FK SQL.

## 7.16 Join Table

**File:** `studentcourse/entity/Student.java`

**Table:** `student_courses(student_id, course_id)`

**What it demonstrates:** A relational database represents a direct N-N relationship with an extra table containing two FKs.

**How to verify:** Inspect rows and constraints in `student_courses` after associating a student and course.

## 7.17 @JoinTable

**File:** `studentcourse/entity/Student.java`

**Field:** `courses`

**Relevant code:**

```java
@JoinTable(
    name = "student_courses",
    joinColumns = @JoinColumn(name = "student_id"),
    inverseJoinColumns = @JoinColumn(name = "course_id")
)
```

**What it demonstrates:** `joinColumns` is the FK back to the current entity (`Student`); `inverseJoinColumns` is the FK to the opposite entity (`Course`).

**How to verify:** Compare the annotations with the two generated FK columns.

## 7.18 When to avoid @ManyToMany

**Files:** this guide and `enrollment/controller/EnrollmentController.java`

**Endpoint:** `POST /practice/enrollments`

**What it demonstrates:** Direct `@ManyToMany` is convenient only while the join relation stores the two FKs. When it needs business fields such as `enrolledAt`, `status`, `grade`, or `assignedBy`, model the relation as a real entity.

**How to verify:** Compare `student_courses`, which contains relationship keys only, with `enrollments`, which also contains business columns.

## 7.19 Intermediate Entity

**File:** `enrollment/entity/Enrollment.java`

**Fields:** `student`, `course`, `enrolledAt`, `status`, `grade`

**Relevant code:**

```java
@Entity
@Table(name = "enrollments")
public class Enrollment {
    @ManyToOne private EnrollmentStudent student;
    @ManyToOne private EnrollmentCourse course;
    private LocalDateTime enrolledAt;
    private String status;
    private String grade;
}
```

**What it demonstrates:** The relationship row gains identity and business data, replacing a direct Many-to-Many with two Many-to-One relationships.

**How to verify:** Create both parties, call `POST /practice/enrollments`, and inspect the resulting `enrollments` row.

## 7.20 Mapping relationships according to the database schema

**Files:** all entity files under the four scenario packages

**Mapping:**

```text
departments 1 <- employees.department_id
users 1 <- profiles.user_id (unique)
students N <-> student_courses <-> N courses
enrollment_students 1 <- enrollments.student_id
enrollment_courses  1 <- enrollments.course_id
```

**What it demonstrates:** Entity annotations should describe the schema that actually owns keys and relationship rows, rather than being chosen only for convenient Java navigation.

**How to verify:** Compare the map above with PostgreSQL metadata and Hibernate startup DDL.

## Expected PostgreSQL schema

```text
departments
  id PK
  name

employees
  id PK
  name
  department_id FK -> departments.id

users
  id PK
  name

profiles
  id PK
  bio
  user_id UNIQUE FK -> users.id

students
  id PK
  name

courses
  id PK
  title

student_courses
  student_id FK -> students.id
  course_id FK -> courses.id
  PK/unique pair generated by Hibernate

enrollment_students
  id PK
  name

enrollment_courses
  id PK
  title

enrollments
  id PK
  student_id FK -> enrollment_students.id
  course_id FK -> enrollment_courses.id
  enrolled_at
  status
  grade
```

## Suggested API experiment order

### Department / Employee

1. `POST /practice/departments`
2. `POST /practice/departments/{departmentId}/employees`
3. `POST /practice/departments/{departmentId}/employees/helper`
4. `GET /practice/departments/{departmentId}`
5. `GET /practice/employees/{employeeId}`
6. `DELETE /practice/departments/{departmentId}/employees/{employeeId}/helper`

### User / Profile

1. `POST /practice/users`
2. `POST /practice/users/{userId}/profile`
3. `GET /practice/users/{userId}`

### Student / Course

1. `POST /practice/students`
2. `POST /practice/courses`
3. `POST /practice/students/{studentId}/courses/{courseId}`
4. `GET /practice/students/{studentId}`

### Intermediate Enrollment entity

1. `POST /practice/enrollment-students`
2. `POST /practice/enrollment-courses`
3. `POST /practice/enrollments`
4. `GET /practice/enrollments/{enrollmentId}`

## Entity relationships versus API JSON

Bidirectional entity graphs can recurse indefinitely during JSON serialization:

```text
Department -> Employee -> Department -> Employee -> ...
```

This project returns response DTOs containing IDs and selected fields. Database relationship mapping and HTTP response serialization are separate concerns; JSON recursion is not the topic of Part 7.

# File -> Knowledge Map

```text
departmentemployee/entity/Department.java
-> @OneToMany
-> mappedBy
-> inverse side
-> addEmployee/removeEmployee helpers
-> bidirectional synchronization

departmentemployee/entity/Employee.java
-> @ManyToOne
-> @JoinColumn
-> department_id foreign key
-> owning side

userprofile/entity/User.java + Profile.java
-> @OneToOne
-> profiles.user_id unique foreign key
-> inverse and owning sides

studentcourse/entity/Student.java + Course.java
-> @ManyToMany
-> @JoinTable
-> joinColumns/inverseJoinColumns
-> student_courses join table

enrollment/entity/Enrollment.java
-> intermediate entity replacing direct ManyToMany
-> two unidirectional @ManyToOne mappings
-> enrolledAt/status/grade business fields

scenario service classes
-> create and read relationships through repository operations
-> explicitly save owning entities; no cascade is configured

scenario controller classes
-> every endpoint comment identifies its Part 7 concepts and service path

scenario dto packages
-> non-recursive API boundary

PART_7_ENTITY_RELATIONSHIPS_GUIDE.md
-> complete checklist, schema, API, and file knowledge map
```

## Part 7 boundary

This project does not teach cascade, fetch strategies, fetch joins, `@EntityGraph`, N+1, pagination, projections, auditing, locking, custom JPQL, or native SQL. A few service methods use a normal transaction only so collection-based relationship experiments run inside one persistence context; transaction behavior itself is not a lesson in this project.
