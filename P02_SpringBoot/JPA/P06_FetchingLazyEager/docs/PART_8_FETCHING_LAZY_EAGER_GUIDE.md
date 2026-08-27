# Part 8 - Fetching: LAZY & EAGER

Base URL: `http://localhost:8087/practice`

| Checklist | Concept | File | Method / Field / Endpoint |
| --- | --- | --- | --- |
| 8.1 | Fetch Strategy | `service/FetchStrategyService.java` | `findBasicEmployee()`, `inspectFindAll()` |
| 8.2 | `FetchType.LAZY` | `entity/Employee.java` | `department`; `GET /lazy/employees/{id}/department` |
| 8.3 | `FetchType.EAGER` | `eagerdemo/entity/EagerEmployee.java` | `department`; `GET /eager-demo/employees/{id}` |
| 8.4 | Default fetch of `@ManyToOne` | `entity/Employee.java` | comment and explicit `fetch = LAZY` override |
| 8.5 | Default fetch of `@OneToMany` | `entity/Department.java` | `employees`; `GET /lazy/departments/{id}/employees` |
| 8.6 | Proxy / Lazy Loading | `service/LazyLoadingService.java` | `inspectDepartmentProxy()` |
| 8.7 | Lazy query timing | `service/LazyLoadingService.java` | `demonstrateEmployeeLazyDepartment()`, `demonstrateDepartmentLazyEmployees()` |
| 8.8 | `LazyInitializationException` | `service/DetachedEmployeeLoader.java`, `service/LazyInitializationExceptionService.java` | `loadEmployeeWithoutDepartment()`, `demonstrate()` |
| 8.9 | Why not overuse EAGER | `service/FetchStrategyService.java`, `eagerdemo/service/EagerDemoService.java` | `findBasicEmployee()`, `loadEagerEmployee()` |
| 8.10 | Fetch Join | `repository/EmployeeRepository.java` | `findByIdWithDepartment()` |
| 8.11 | `JOIN FETCH` | `repository/EmployeeRepository.java` | `JOIN FETCH e.department`; `GET /fetch-join/employees/{id}` |
| 8.12 | `@EntityGraph` | `repository/EmployeeRepository.java` | `findByIdWithDepartmentEntityGraph()` |
| 8.13 | Fetch by use case | `controller/FetchPracticeController.java` | three `/use-cases/employees/{id}/...` endpoints |

Paths in this guide are relative to `src/main/java/com/example/P06_FetchingLazyEager/` unless stated otherwise.

## Setup and seed data

Create the PostgreSQL database:

```sql
CREATE DATABASE jpa_fetching_practice;
```

Run with Java 21:

```text
mvnw.cmd spring-boot:run
```

Create a Department:

```http
POST /practice/departments
Content-Type: application/json

{
  "name": "Engineering",
  "description": "Builds the product"
}
```

Create an Employee using the returned Department id:

```http
POST /practice/employees
Content-Type: application/json

{
  "name": "An",
  "email": "an.p06@example.com",
  "salary": 2500.00,
  "departmentId": 1
}
```

The project does not use cascade. Department and Employee are saved explicitly.

## 8.1 Fetch Strategy

**Files:** `entity/Employee.java`, `entity/Department.java`, `service/FetchStrategyService.java`

A fetch strategy determines when relationship state becomes available:

```text
LAZY  -> relationship state is deferred until it is needed
EAGER -> relationship state must be fetched as part of loading the owner
```

EAGER does not promise one JOIN SQL. Hibernate may use a join or additional SQL depending on the loading context. Call `GET /practice/find-all/initialization` to see that loading all Employee entities does not mean initializing their LAZY departments.

## 8.2 FetchType.LAZY

**File:** `entity/Employee.java`

**Field:** `department`

```java
@ManyToOne(fetch = FetchType.LAZY)
@JoinColumn(name = "department_id", nullable = false)
private Department department;
```

**Runtime:** `LazyLoadingService.demonstrateEmployeeLazyDepartment()`

Call `GET /practice/lazy/employees/{id}/department`. The response should report `initializedBeforeAccess=false` and `initializedAfterAccess=true`. Match the second SELECT with the `=== ACCESSING DEPARTMENT.NAME ===` log marker.

## 8.3 FetchType.EAGER

**File:** `eagerdemo/entity/EagerEmployee.java`

```java
@ManyToOne(fetch = FetchType.EAGER)
@JoinColumn(name = "department_id", nullable = false)
private EagerDepartment department;
```

This model is deliberately isolated in `eager_departments` and `eager_employees`; it does not change the main LAZY model.

Create and reload an example:

```http
POST /practice/eager-demo
Content-Type: application/json

{
  "departmentName": "Eager Sales",
  "employeeName": "Binh",
  "employeeEmail": "binh.eager.p06@example.com",
  "salary": 2200.00
}
```

Then call `GET /practice/eager-demo/employees/{id}`. `departmentInitialized` should be `true`. Observe the actual SQL rather than assuming it must always be one join.

## 8.4 Default fetch of @ManyToOne

**File:** `entity/Employee.java`

JPA's default for `@ManyToOne` is EAGER:

```java
@ManyToOne
// fetch default is FetchType.EAGER
```

P06 explicitly overrides it with `fetch = FetchType.LAZY` because the main design lesson is to fetch according to each use case. The isolated EAGER model shows the default-style behavior without changing the main model.

## 8.5 Default fetch of @OneToMany

**File:** `entity/Department.java`

**Field:** `employees`

```java
@OneToMany(mappedBy = "department", fetch = FetchType.LAZY)
private List<Employee> employees;
```

JPA's default for `@OneToMany` is LAZY. It is written explicitly here to make the lesson visible. Call `GET /practice/lazy/departments/{id}/employees`; the collection query occurs at `employees.size()`.

## 8.6 Proxy / Lazy Loading

**File:** `service/LazyLoadingService.java`

**Method:** `inspectDepartmentProxy()`

```java
Department department = employee.getDepartment();
String runtimeClass = department.getClass().getName();
boolean before = Hibernate.isInitialized(department);
String departmentName = department.getName();
boolean after = Hibernate.isInitialized(department);
```

Call `GET /practice/proxy/employees/{id}`. A provider-specific runtime class may be visible, but correctness does not depend on its class name. The important evidence is initialization changing from false to true after state is accessed.

Mental model:

```text
Employee row:
department_id = 10

Employee entity:
department
    ↓
lazy proxy/reference for Department#10

Department full state has not been loaded yet.

Access department.getName()
    ↓ Hibernate needs actual state
SELECT ... FROM departments WHERE id = 10
    ↓
relationship initialized
```

## 8.7 When Lazy Loading queries the database

**File:** `service/LazyLoadingService.java`

**ManyToOne method:** `demonstrateEmployeeLazyDepartment()`

```text
find Employee -> Employee SELECT
get Department reference -> no Department field access yet
department.getName() -> Department SELECT
```

Call `GET /practice/lazy/employees/{id}/department`.

**OneToMany method:** `demonstrateDepartmentLazyEmployees()`

```text
find Department -> Department SELECT
get employees collection -> not initialized
employees.size() -> Employee collection SELECT
```

Call `GET /practice/lazy/departments/{id}/employees`.

The log markers immediately before each access make the SQL boundary visible.

## 8.8 LazyInitializationException

**Files:** `service/DetachedEmployeeLoader.java`, `service/LazyInitializationExceptionService.java`

`DetachedEmployeeLoader.loadEmployeeWithoutDepartment()` loads Employee inside a real Spring transaction and deliberately does not initialize Department. `LazyInitializationExceptionService.demonstrate()` then accesses the relation after that method and transaction have ended.

```text
LAZY relationship
        ↓
Persistence Context alive?
    /           \
  YES            NO
   |              |
can query DB      cannot lazy load
                  ↓
        LazyInitializationException
```

Call `GET /practice/lazy-exception/employees/{id}`. The endpoint catches the expected exception and returns it as a learning DTO, so the application remains running. `spring.jpa.open-in-view=false` makes the boundary deterministic.

## 8.9 Why EAGER should not be overused

**Files:** `service/FetchStrategyService.java`, `eagerdemo/service/EagerDemoService.java`

Call `GET /practice/use-cases/employees/{id}/basic`. This response needs only `id`, `name`, and `email`; fetching Department would be unnecessary work. Compare its SQL with `GET /practice/eager-demo/employees/{id}`.

EAGER is not inherently wrong, but applying it globally makes unrelated use cases pay for relationship data they do not need. It also does not guarantee a specific number or shape of SQL statements.

Important clarification:

```text
employeeRepository.findAll()
does NOT mean
"load every relationship completely"
```

It loads Employee entities. Their LAZY `department` references can remain uninitialized, as shown by `GET /practice/find-all/initialization`.

## 8.10 Fetch Join

**File:** `repository/EmployeeRepository.java`

**Method:** `findByIdWithDepartment()`

A fetch join both participates in the query and initializes the selected relationship for this result. It is useful when the current use case needs related state.

## 8.11 JOIN FETCH

```java
@Query("""
        SELECT e
        FROM Employee e
        JOIN FETCH e.department
        WHERE e.id = :id
        """)
Optional<Employee> findByIdWithDepartment(@Param("id") Long id);
```

Call `GET /practice/fetch-join/employees/{id}`. The Department is initialized by the repository query, so DTO mapping does not trigger a later lazy Department SELECT.

```text
Normal LAZY query:
SELECT Employee
    ↓
Employee.department not initialized
    ↓ later access
SELECT Department

JOIN FETCH:
SELECT Employee + Department
    ↓
Department already initialized
```

`JOIN` can be used for query logic; `JOIN FETCH` additionally instructs fetching of the relationship.

## 8.12 @EntityGraph

**File:** `repository/EmployeeRepository.java`

**Method:** `findByIdWithDepartmentEntityGraph()`

```java
@EntityGraph(attributePaths = "department")
@Query("""
        SELECT e
        FROM Employee e
        WHERE e.id = :id
        """)
Optional<Employee> findByIdWithDepartmentEntityGraph(@Param("id") Long id);
```

Call `GET /practice/entity-graph/employees/{id}`.

```text
JOIN FETCH  -> fetching requirement is expressed inside JPQL
@EntityGraph -> fetching plan is declared on the repository method
```

Both endpoints return the same DTO shape. Neither technique is universally superior; choose the one that makes the use case clearest.

## 8.13 Fetch according to each use case

**Files:** `controller/FetchPracticeController.java`, `repository/EmployeeRepository.java`

```text
Use Case A
GET /practice/use-cases/employees/{id}/basic
Employee basic information
-> Department not needed
-> regular findById, keep relation lazy

Use Case B
GET /practice/use-cases/employees/{id}/detail
Employee plus Department detail
-> JOIN FETCH

Use Case C
GET /practice/use-cases/employees/{id}/detail-entity-graph
Same required detail
-> @EntityGraph
```

Multiple repository methods for different data needs are intentional. The goal is to fetch what the use case needs, not to make every relationship globally EAGER.

## SQL and expected schema

SQL, formatting, and bind logging are enabled in `src/main/resources/application.properties`. OSIV is disabled there too.

Expected tables:

```text
departments
employees              -> department_id FK
eager_departments
eager_employees        -> department_id FK
```

Entity relationships and API serialization are separate concerns. Controllers return DTOs and never expose the bidirectional entity graph.

# File → Knowledge Map

```text
entity/Employee.java
-> ManyToOne LAZY
-> JPA default ManyToOne EAGER note
-> Department lazy relationship

entity/Department.java
-> OneToMany LAZY
-> JPA default OneToMany LAZY note

service/LazyLoadingService.java
-> proxy/runtime class
-> Hibernate.isInitialized()
-> ManyToOne and OneToMany SQL timing

service/DetachedEmployeeLoader.java
service/LazyInitializationExceptionService.java
-> transaction ends before lazy access
-> deterministic LazyInitializationException

eagerdemo/entity/EagerEmployee.java
eagerdemo/service/EagerDemoService.java
-> isolated EAGER comparison

repository/EmployeeRepository.java
service/FetchJoinService.java
-> JOIN FETCH

repository/EmployeeRepository.java
service/EntityGraphService.java
-> @EntityGraph

service/FetchStrategyService.java
-> basic use case
-> findAll does not initialize every relationship

controller/FetchPracticeController.java
-> runnable API index
-> every endpoint identifies its Part 8 concept and service method
```

No cascade, orphan removal, N+1 optimization lesson, pagination, caching, locking, or Part 9 topic is introduced.
