# Part 11 - N+1 Query Problem Guide

| Checklist | Concept | File | Method / Endpoint |
| --------- | ------- | ---- | ----------------- |
| 11.1 | N+1 | `docs/PART_11_N_PLUS_ONE_GUIDE.md` | `GET /practice/nplus1/one-to-many` |
| 11.2 | Why N+1 happens | `service/NPlusOneService.java` | `oneToManyNPlusOne()` |
| 11.3 | OneToMany N+1 | `service/NPlusOneService.java` | `oneToManyNPlusOne()` / `GET /practice/nplus1/one-to-many` |
| 11.4 | ManyToOne N+1 | `service/NPlusOneService.java` | `manyToOneNPlusOne()` / `GET /practice/nplus1/many-to-one` |
| 11.5 | SQL log detection | `application.properties`, `service/NPlusOneService.java` | SQL logs plus `=== ... ===` markers |
| 11.6 | Fetch Join | `repository/DepartmentRepository.java`, `repository/EmployeeRepository.java`, `service/FetchJoinService.java` | `/practice/solutions/fetch-join/departments`, `/practice/solutions/fetch-join/employees` |
| 11.7 | EntityGraph | `repository/DepartmentRepository.java`, `repository/EmployeeRepository.java`, `service/EntityGraphService.java` | `/practice/solutions/entity-graph/departments`, `/practice/solutions/entity-graph/employees` |
| 11.8 | DTO Projection | `repository/EmployeeRepository.java`, `service/ProjectionService.java` | `findEmployeeDepartmentViews()` / `GET /practice/solutions/projection/employees` |
| 11.9 | Batch Fetching | `service/BatchFetchingService.java` | `explainBatchFetching()` / `GET /practice/solutions/batch-fetching` |
| 11.10 | EAGER is not solution | `service/EagerMisconceptionService.java` | `employeeBasicInfoDoesNotNeedDepartment()` / `GET /practice/eager-is-not-solution/employees/basic` |

## Run Setup

Database:

```text
jpa_nplus1_practice
```

Application properties enable SQL visibility:

```properties
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql=true
spring.jpa.open-in-view=false
spring.jpa.properties.hibernate.generate_statistics=true
logging.level.org.hibernate.orm.jdbc.bind=TRACE
```

Seed data:

```http
POST /practice/seed
```

This creates:

```text
Department A
├── Employee A1
├── Employee A2
└── Employee A3

Department B
├── Employee B1
├── Employee B2
└── Employee B3

Department C
├── Employee C1
├── Employee C2
└── Employee C3
```

## 11.1 - What N+1 Means

N+1 does NOT mean N+1 database rows.

It means:

```text
1 query to load a list of root entities
+
additional queries generated while loading/accessing related data
```

Example:

```text
1 query → load 5 Departments
5 queries → load Employees for each Department

total = 6 queries
```

## 11.2 - Why N+1 Happens

Baseline code shape in `NPlusOneService.oneToManyNPlusOne()`:

```java
List<Department> departments = departmentRepository.findAll();

for (Department department : departments) {
    department.getEmployees().size();
}
```

Runtime behavior:

```text
findAll()
→ loads Departments

employees is LAZY
→ collection not loaded yet

loop accesses employees
→ Hibernate initializes each collection separately
```

LAZY itself is not automatically bad. The problem appears when a use case loads many root entities and then repeatedly touches lazy relationships one by one.

## 11.3 - OneToMany N+1

Endpoint:

```http
GET /practice/nplus1/one-to-many
GET /practice/compare/one-to-many/nplus1
```

Log markers:

```text
=== ROOT DEPARTMENTS LOADED ===
=== ACCESSING EMPLOYEES FOR DEPARTMENT <id> ===
```

Expected SQL shape:

```text
SELECT ... FROM departments

SELECT ... FROM employees WHERE department_id = ?
SELECT ... FROM employees WHERE department_id = ?
SELECT ... FROM employees WHERE department_id = ?
...
```

Diagram:

```text
OneToMany N+1

SELECT Departments        ← 1

Department 1
↓ getEmployees()
SELECT Employees          ← +1

Department 2
↓ getEmployees()
SELECT Employees          ← +1

Department 3
↓ getEmployees()
SELECT Employees          ← +1
```

## 11.4 - ManyToOne N+1

Endpoint:

```http
GET /practice/nplus1/many-to-one
GET /practice/compare/many-to-one/nplus1
```

Baseline code shape:

```java
List<Employee> employees = employeeRepository.findAll();

for (Employee employee : employees) {
    employee.getDepartment().getName();
}
```

Log markers:

```text
=== ROOT EMPLOYEES LOADED ===
=== ACCESSING DEPARTMENT FOR EMPLOYEE <id> ===
```

Expected SQL shape:

```text
SELECT ... FROM employees

SELECT ... FROM departments WHERE id = ?
SELECT ... FROM departments WHERE id = ?
...
```

The number of additional queries may be lower than the Employee count when multiple Employees reference the same Department already present in Hibernate's first-level cache.

## 11.5 - Detecting N+1 Via SQL Logs

Strong signal:

```text
one root SELECT
↓
many repeated SELECT statements
↓
same SQL shape
↓
only parameter changes
```

The API response includes `QueryExperimentResult.queryCount` from Hibernate statistics. Treat this as a learning aid. SQL logs are still the main teaching tool because Hibernate SQL shape can vary by version and dialect.

# BEFORE vs AFTER

## Before

```text
1 root SELECT
N relationship SELECTs
```

## Fetch Join

```text
single joined/fetch query for the use case
```

## EntityGraph

```text
relationship eagerly fetched for this repository method
```

## Projection

```text
one query selecting only required columns
```

Do not promise an exact SQL count where Hibernate implementation details can vary. Focus on the query pattern.

## 11.6 - Fetch Join

OneToMany endpoint:

```http
GET /practice/solutions/fetch-join/departments
GET /practice/compare/one-to-many/fetch-join
```

Repository method:

```java
@Query("""
    SELECT DISTINCT d
    FROM Department d
    LEFT JOIN FETCH d.employees
""")
List<Department> findAllWithEmployeesFetchJoin();
```

ManyToOne endpoint:

```http
GET /practice/solutions/fetch-join/employees
GET /practice/compare/many-to-one/fetch-join
```

Repository method:

```java
@Query("""
    SELECT e
    FROM Employee e
    JOIN FETCH e.department
""")
List<Employee> findAllWithDepartmentFetchJoin();
```

`DISTINCT` is commonly used with a collection fetch join to avoid duplicate root entity results when a Department has multiple Employees.

Diagram:

```text
Fetch Join

SELECT Department + Employees
↓
relationship already loaded
↓
no per-Department lazy SELECT
```

`JOIN FETCH` solves specific fetch needs. It is not a universal replacement for every query.

## 11.7 - EntityGraph

Endpoints:

```http
GET /practice/solutions/entity-graph/departments
GET /practice/solutions/entity-graph/employees
GET /practice/compare/one-to-many/entity-graph
GET /practice/compare/many-to-one/entity-graph
```

Repository methods:

```java
@EntityGraph(attributePaths = "employees")
@Query("SELECT d FROM Department d")
List<Department> findAllWithEmployeesEntityGraph();

@EntityGraph(attributePaths = "department")
@Query("SELECT e FROM Employee e")
List<Employee> findAllWithDepartmentEntityGraph();
```

Comparison:

```text
JOIN FETCH
→ fetch strategy expressed in JPQL

@EntityGraph
→ fetch plan declared on repository method
```

Do not claim one is universally better. Both are use-case-specific fetch plans.

## 11.8 - DTO Projection

Endpoint:

```http
GET /practice/solutions/projection/employees
GET /practice/compare/projection
```

DTO:

```java
public record EmployeeDepartmentView(
        Long employeeId,
        String employeeName,
        String departmentName
) {}
```

Projection behavior:

```text
DTO Projection
→ not a Managed Entity
→ no dirty checking
→ intended mainly for read/query use cases
```

Diagram:

```text
DTO Projection

SELECT only required columns
↓
construct DTO
↓
no full Entity graph required
```

Lesson:

```text
If the use case only needs selected fields,
do not necessarily load Entity + lazy relationship graph.
```

Projection is not the same thing as fetch join. Projection returns read data directly; fetch join returns managed Entities with selected relationships initialized.

## 11.9 - Batch Fetching Concept

Endpoint:

```http
GET /practice/solutions/batch-fetching
```

This lab keeps batch fetching documentation-only so it does not hide the baseline N+1 problem.

Without batching:

```text
SELECT relation WHERE parent_id = 1
SELECT relation WHERE parent_id = 2
SELECT relation WHERE parent_id = 3
SELECT relation WHERE parent_id = 4
```

With batch fetching:

```text
SELECT relation
WHERE parent_id IN (?, ?, ?, ?)
```

Diagram:

```text
Batch Fetching

lazy relationships required
↓
Hibernate groups multiple parent IDs
↓
loads them in batches
```

Batch fetching does not necessarily turn everything into one query. It reduces the number of lazy-loading queries by loading relationships in groups.

## 11.10 - Why EAGER Is Not The Solution

Endpoint:

```http
GET /practice/eager-is-not-solution/employees/basic
```

Use case:

```text
GET employee basic info

needs:
id
name
email

does not need:
Department details
```

N+1 exists, but changing every relation to EAGER is not a proper global fix.

Reasons:

```text
- unrelated use cases load unnecessary relationships
- query behavior becomes harder to control
- memory/data transfer may increase
- EAGER does not guarantee one efficient SQL query
- EAGER itself can still lead to additional selects depending on query/loading strategy
```

The main mappings remain:

```java
@OneToMany(mappedBy = "department", fetch = FetchType.LAZY)
private List<Employee> employees = new ArrayList<>();

@ManyToOne(fetch = FetchType.LAZY)
private Department department;
```

## Misconceptions Addressed

1. N+1 is about query count, not row count.
2. LAZY itself is not automatically bad.
3. OneToMany and ManyToOne can both produce N+1.
4. One query followed by many similar queries with different IDs is a common N+1 signal.
5. `JOIN FETCH` solves specific fetch needs, not every query universally.
6. `@EntityGraph` is another use-case-specific fetch plan.
7. DTO Projection is useful when only read data is required.
8. Batch fetching reduces query count by grouping lazy loads.
9. EAGER everywhere is not a proper N+1 strategy.
10. The correct fetch strategy depends on the use case.

# File → Knowledge Map

```text
src/main/java/com/example/P08_NPlusOnePractice/entity/Department.java
→ OneToMany LAZY
→ baseline collection N+1 source

src/main/java/com/example/P08_NPlusOnePractice/entity/Employee.java
→ ManyToOne LAZY
→ baseline to-one N+1 source

src/main/java/com/example/P08_NPlusOnePractice/service/NPlusOneService.java
→ reproduces N+1
→ logs root query and relationship access boundaries

src/main/java/com/example/P08_NPlusOnePractice/repository/DepartmentRepository.java
→ Department fetch join
→ Department EntityGraph

src/main/java/com/example/P08_NPlusOnePractice/repository/EmployeeRepository.java
→ ManyToOne fetch join
→ ManyToOne EntityGraph
→ DTO projection

src/main/java/com/example/P08_NPlusOnePractice/service/FetchJoinService.java
→ before/after fetch join comparison behavior

src/main/java/com/example/P08_NPlusOnePractice/service/EntityGraphService.java
→ before/after EntityGraph comparison behavior

src/main/java/com/example/P08_NPlusOnePractice/service/ProjectionService.java
→ projection read use case
→ employee basic-info use case

src/main/java/com/example/P08_NPlusOnePractice/service/BatchFetchingService.java
→ batch fetching concept without changing baseline runtime behavior

src/main/java/com/example/P08_NPlusOnePractice/service/EagerMisconceptionService.java
→ why EAGER everywhere wastes data for basic read use cases

src/main/java/com/example/P08_NPlusOnePractice/controller/NPlusOnePracticeController.java
→ runnable comparison endpoints
→ Part 11 checklist comments before every endpoint

src/main/resources/application.properties
→ SQL logs
→ bind parameter logs
→ Hibernate statistics query counting

docs/PART_11_N_PLUS_ONE_GUIDE.md
→ SQL pattern explanations
→ before/after comparison
```
