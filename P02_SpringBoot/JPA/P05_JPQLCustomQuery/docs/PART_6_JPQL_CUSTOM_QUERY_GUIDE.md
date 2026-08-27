# Part 6 - JPQL & Custom Query Guide

## Checklist map

| Checklist | Concept | File | Method / Query / Endpoint |
| --- | --- | --- | --- |
| 6.1 | JPQL | `EmployeeRepository.java` | `findActiveEmployees()` |
| 6.2 | JPQL vs SQL | `EmployeeRepository.java` | `findActiveEmployees()`, `findActiveEmployeesNative()` |
| 6.3 | Entity/field instead of table/column | `EmployeeRepository.java` | `findActiveEmployees()` |
| 6.4 | `@Query` | `EmployeeRepository.java` | all declared query methods |
| 6.5 | Positional parameters | `EmployeeRepository.java` | `findByNameAndActivePositional()` |
| 6.6 | Named parameters | `EmployeeRepository.java` | `findByNameAndActiveNamed()` |
| 6.7 | `@Param` | `EmployeeRepository.java` | `findByNameAndActiveNamed()` |
| 6.8 | Select query | `EmployeeRepository.java` | `findAllEmployeesJpql()`, `findActiveEmployees()` |
| 6.9 | Join query | `EmployeeRepository.java` | `findEmployeesByDepartmentName()` |
| 6.10 | Aggregate query | `EmployeeRepository.java` | aggregate methods, `findDepartmentStats()` |
| 6.11 | `COUNT` / `SUM` / `AVG` | `EmployeeRepository.java` | `countEmployeesJpql()`, `sumEmployeeSalary()`, `averageEmployeeSalary()` |
| 6.12 | DTO projection | `EmployeeRepository.java` | `findEmployeeSummaries()`, `findDepartmentStats()` |
| 6.13 | Native query | `EmployeeRepository.java` | `findActiveEmployeesNative()` |
| 6.14 | `nativeQuery = true` | `EmployeeRepository.java` | `findActiveEmployeesNative()` |
| 6.15 | JPQL vs Native SQL choice | this guide | section 6.15 |
| 6.16 | Modifying query | `EmployeeRepository.java` | deactivate/update/delete/bulk methods |
| 6.17 | `@Modifying` | `EmployeeRepository.java` | modifying method annotations |
| 6.18 | UPDATE using query | `EmployeeRepository.java` | `deactivateEmployee()`, `updateSalary()` |
| 6.19 | DELETE using query | `EmployeeRepository.java` | `deleteInactiveEmployees()` |
| 6.20 | Bulk update vs Persistence Context | `BulkQueryService.java` | `demonstrateStaleContext()`, `demonstrateAutomaticClear()` |

Java paths in this guide are relative to:

```text
src/main/java/com/example/P05_JPQLCustomQuery/
```

## Prepare experiment data

Create a department:

```http
POST /practice/departments
Content-Type: application/json

{
  "name": "Engineering"
}
```

Create at least two employees with unique emails:

```http
POST /practice/employees
Content-Type: application/json

{
  "name": "Alice",
  "email": "alice@example.com",
  "salary": 1500.00,
  "active": true,
  "departmentId": 1
}
```

Use one active employee for bulk experiments. Create another inactive employee for the DELETE experiment so important test data is not removed accidentally.

## 6.1 JPQL concept

**File:** `repository/EmployeeRepository.java`

**Method:** `findActiveEmployees()`

**JPQL:**

```java
SELECT e
FROM Employee e
WHERE e.active = true
```

**Concept:** JPQL describes a query through mapped entities. Hibernate parses it and generates SQL for PostgreSQL.

**Call:** `GET /practice/jpql/active`

**Observe:** The repository contains JPQL, while the console shows generated SQL against `employees`.

## 6.2 JPQL vs SQL

**File:** `repository/EmployeeRepository.java`

**Methods:** `findActiveEmployees()`, `findActiveEmployeesNative()`

**Comparison:**

```java
// JPQL
SELECT e FROM Employee e WHERE e.active = true

// Native PostgreSQL SQL
SELECT * FROM employees WHERE active = true
```

```text
JPQL       -> Entity + Java field -> Hibernate converts it to SQL
Native SQL -> table + column      -> database SQL is executed directly
```

**Call:** Compare `GET /practice/jpql/active` with `GET /practice/native/active`.

**Expect:** Equivalent employee DTO results for this simple example.

## 6.3 JPQL queries Entity/field instead of Table/column

**File:** `repository/EmployeeRepository.java`

**Method:** `findActiveEmployees()`

**Relevant code:**

```java
FROM Employee e
WHERE e.active = true
```

**Important:** `Employee` is the entity name and `active` is the Java field. This JPQL does not reference the physical `employees` table.

**Call:** `GET /practice/jpql/active`

**Observe:** Hibernate translates the names from the entity mapping into table/column SQL.

## 6.4 @Query

**File:** `repository/EmployeeRepository.java`

**Methods:** all custom query methods

**Relevant code:**

```java
@Query("""
    SELECT e
    FROM Employee e
""")
List<Employee> findAllEmployeesJpql();
```

**Concept:** `@Query` attaches explicit JPQL or SQL to a repository method. This laboratory does not rely only on derived query names.

**Call:** `GET /practice/jpql/all`

**Expect:** All employee rows returned as DTOs.

## 6.5 Positional Parameters

**File:** `repository/EmployeeRepository.java`

**Method:** `findByNameAndActivePositional(...)`

**JPQL:**

```java
WHERE e.name = ?1
  AND e.active = ?2
```

```text
?1 -> first Java method parameter: name
?2 -> second Java method parameter: active
```

**Call:** `GET /practice/jpql/positional?name=Alice&active=true`

**Observe:** Bind logs show the supplied name and boolean values.

## 6.6 Named Parameters

**File:** `repository/EmployeeRepository.java`

**Method:** `findByNameAndActiveNamed(...)`

**JPQL:**

```java
WHERE e.name = :name
  AND e.active = :active
```

**Concept:** Names express the role of each value and are normally easier to read and maintain than numeric positions.

**Call:** `GET /practice/jpql/named?name=Alice&active=true`

**Expect:** The same logical result as the positional endpoint.

## 6.7 @Param

**File:** `repository/EmployeeRepository.java`

**Method:** `findByNameAndActiveNamed(...)`

**Relevant code:**

```java
@Param("name") String name,
@Param("active") boolean active
```

**Concept:** `@Param` connects a Java parameter to the matching `:name` or `:active` JPQL placeholder.

**Call:** `GET /practice/jpql/named?name=Alice&active=true`

**Observe:** Changing argument order would not change the meaning as long as the `@Param` names remain correct.

## 6.8 Select Query

**File:** `repository/EmployeeRepository.java`

**Methods:** `findAllEmployeesJpql()`, `findActiveEmployees()`

**JPQL:**

```java
SELECT e FROM Employee e
SELECT e FROM Employee e WHERE e.active = true
```

**Call:**

```text
GET /practice/jpql/all
GET /practice/jpql/active
```

**Expect:** The first returns all employees; the second returns active employees only.

## 6.9 Join Query

**File:** `repository/EmployeeRepository.java`

**Method:** `findEmployeesByDepartmentName(...)`

**JPQL:**

```java
SELECT e
FROM Employee e
JOIN e.department d
WHERE d.name = :departmentName
```

**Important:** `JOIN e.department` follows the Java relationship. It does not write a raw database `ON employees.department_id = departments.id` condition.

**Call:** `GET /practice/jpql/join?departmentName=Engineering`

**Observe:** Hibernate generates the actual relational JOIN in SQL.

## 6.10 Aggregate Query

**File:** `repository/EmployeeRepository.java`

**Methods:** aggregate methods and `findDepartmentStats()`

**Relevant code:**

```java
SELECT COUNT(e) FROM Employee e
SELECT SUM(e.salary) FROM Employee e
SELECT AVG(e.salary) FROM Employee e
```

**Call:**

```text
GET /practice/jpql/aggregate
GET /practice/jpql/department-stats
```

**Expect:** Global totals from the first endpoint and grouped employee counts from the second.

## 6.11 COUNT / SUM / AVG

**File:** `repository/EmployeeRepository.java`

**Methods:**

```text
countEmployeesJpql()
sumEmployeeSalary()
averageEmployeeSalary()
```

**Result mapping:**

```java
long count
BigDecimal sum
Double average
```

**Call:** `GET /practice/jpql/aggregate`

**Empty-table behavior:** JPQL `SUM` and `AVG` can return `null`; `EmployeeQueryService.aggregate()` converts those empty results to zero for a stable API response.

## 6.12 DTO Projection using JPQL

**Files:**

- `dto/EmployeeSummary.java`
- `dto/DepartmentStats.java`
- `repository/EmployeeRepository.java`

**Methods:** `findEmployeeSummaries()`, `findDepartmentStats()`

**JPQL constructor projection:**

```java
SELECT new com.example.P05_JPQLCustomQuery.dto.EmployeeSummary(
    e.id,
    e.name,
    e.email
)
FROM Employee e
```

The fully qualified class name is the actual P05 package.

**Call:**

```text
GET /practice/jpql/projection
GET /practice/jpql/department-stats
```

**Observe:** Hibernate constructs DTOs directly instead of returning full entities.

## 6.13 Native Query

**File:** `repository/EmployeeRepository.java`

**Method:** `findActiveEmployeesNative()`

**SQL:**

```sql
SELECT *
FROM employees
WHERE active = true
```

**Important:** `employees` and `active` are real PostgreSQL table/column names.

**Call:** `GET /practice/native/active`

**Observe:** The SQL is already database SQL; Hibernate does not translate JPQL entity names.

## 6.14 nativeQuery = true

**File:** `repository/EmployeeRepository.java`

**Method:** `findActiveEmployeesNative()`

**Relevant code:**

```java
@Query(value = "...", nativeQuery = true)
```

**Concept:** `nativeQuery = true` tells Spring Data that the string is SQL, not JPQL.

**Call:** `GET /practice/native/active`

**Expect:** Active employees mapped back into `Employee` entities and then response DTOs.

## 6.15 JPQL vs Native SQL - when to use each

Use JPQL when:

- the query naturally follows entities and relationships;
- portability matters;
- no database-specific capability is required.

Use native SQL when:

- a database-specific feature is required;
- SQL needs precise, database-focused optimization;
- JPQL cannot conveniently express the operation.

For this project's active-employee query, JPQL is the more natural choice. The native version exists only for direct comparison.

**Call:** Compare the two active endpoints and their repository query text.

## 6.16 Modifying Query

**Files:**

- `repository/EmployeeRepository.java`
- `service/ModifyingQueryService.java`

**Methods:** deactivate, update salary, delete inactive, and both name bulk updates

**Relevant code:**

```java
@Modifying
@Query("UPDATE Employee e ...")
```

**Concept:** UPDATE and DELETE queries modify rows rather than return entity result sets. Transaction boundaries are controlled in the service layer.

**Call:** `PATCH /practice/modify/{id}/salary`

**Expect:** `affectedRows` is normally 1 for an existing ID and 0 for a missing ID.

## 6.17 @Modifying

**File:** `repository/EmployeeRepository.java`

**Methods:** every JPQL UPDATE/DELETE method

**Relevant code:**

```java
@Modifying
@Query("DELETE FROM Employee e WHERE e.active = false")
int deleteInactiveEmployees();
```

**Important:** `@Modifying` belongs on UPDATE/DELETE methods, not SELECT methods.

**Call:** `DELETE /practice/modify/inactive`

**Observe:** The return value is the number of affected rows.

## 6.18 UPDATE using query

**File:** `repository/EmployeeRepository.java`

**Methods:** `deactivateEmployee()`, `updateSalary()`

**JPQL:**

```java
UPDATE Employee e
SET e.salary = :salary
WHERE e.id = :id
```

**Call:**

```http
PATCH /practice/modify/{id}/salary

{"salary": 2500.00}
```

Or call `PATCH /practice/modify/{id}/deactivate`.

**Observe:** SQL UPDATE plus the affected-row count; no entity is returned by the repository query.

## 6.19 DELETE using query

**File:** `repository/EmployeeRepository.java`

**Method:** `deleteInactiveEmployees()`

**JPQL:**

```java
DELETE FROM Employee e
WHERE e.active = false
```

**Call:** `DELETE /practice/modify/inactive`

**Expect:** All currently inactive employees are removed and their count is returned. Create disposable inactive data before this experiment.

## 6.20 Bulk Update effect on Persistence Context

**Files:**

- `repository/EmployeeRepository.java`
- `service/BulkQueryService.java`
- `dto/BulkUpdateResult.java`
- `dto/AutoClearBulkUpdateResult.java`

### Experiment A - deliberately stale managed entity

**Service method:** `demonstrateStaleContext(...)`

**Repository method:** `bulkUpdateName(...)`

**Call:**

```http
POST /practice/bulk/stale-context/{id}
Content-Type: application/json

{"name": "Name Written By Bulk Query"}
```

**Flow:**

```text
Persistence Context
Employee#1 name = "Old Name"
entityManager.contains(employee) = true

                  bulk JPQL UPDATE
                         |
                         v

Database
employees.name = "Name Written By Bulk Query"

Persistence Context still contains:
Employee#1 name = "Old Name"
```

The service reads the database value with a scalar native query. That read bypasses entity lookup, allowing the response to show simultaneously:

```text
databaseValueAfterBulkUpdate = new name
managedValueAfterBulkUpdate  = old name
managedBeforeClear           = true
```

Then:

```text
entityManager.clear()
        |
        v
old object becomes detached
        |
        v
EntityManager.find() reloads from database
        |
        v
valueAfterClearAndReload = new name
```

**Core rule:** Bulk UPDATE/DELETE does not update the state of already managed entities in the Persistence Context.

### Experiment B - clearAutomatically

**Service method:** `demonstrateAutomaticClear(...)`

**Repository method:** `bulkUpdateNameAndClear(...)`

**Repository annotation:**

```java
@Modifying(clearAutomatically = true)
```

**Call:**

```http
POST /practice/bulk/auto-clear/{id}
Content-Type: application/json

{"name": "Name Written With Auto Clear"}
```

**Expected response relationship:**

```text
managedBeforeQuery = true
managedAfterQuery  = false
oldObjectValueAfterBulkUpdate = old value on the now-detached Java object
valueAfterReload = new database value
```

`clearAutomatically` clears the Persistence Context after the modifying query. It does not mutate the old Java object; it detaches it so a later find performs a reload.

`flushAutomatically = true`, if configured, would flush pending managed changes before running the modifying query. P05 does not enable it because the experiment has no pending entity changes and flushing is not the lesson focus.

## API response boundary

Controllers return DTOs instead of recursive JPA entity graphs. The relationship exists only to support JOIN/aggregate examples and is not expanded into Part 7 relationship theory.

# File -> Knowledge Map

```text
entity/Department.java + entity/Employee.java
-> minimal query domain
-> Employee.department enables JPQL JOIN

repository/EmployeeRepository.java
-> @Query
-> entity/field JPQL
-> positional parameters
-> named parameters and @Param
-> SELECT and JOIN
-> COUNT/SUM/AVG
-> JPQL constructor DTO projection
-> native SQL and nativeQuery=true
-> @Modifying
-> UPDATE and DELETE
-> bulk update with/without automatic clear

service/PracticeDataService.java
-> creates reproducible Department/Employee test data

service/EmployeeQueryService.java
-> runs SELECT/JOIN/aggregate/projection/native query examples

service/ModifyingQueryService.java
-> service transaction boundary for UPDATE/DELETE
-> affected-row responses

service/BulkQueryService.java
-> stale Persistence Context experiment
-> EntityManager.contains()
-> scalar database read
-> clear and reload
-> clearAutomatically experiment

controller/QueryPracticeController.java
-> runnable API index
-> every endpoint maps to Part 6 checklist items

docs/PART_6_JPQL_CUSTOM_QUERY_GUIDE.md
-> complete checklist, experiment instructions, and knowledge map
```

## Part 6 boundary

This project does not teach fetch strategies, fetch joins, `@EntityGraph`, N+1, cascade, orphan removal, pagination, advanced projections, locking, auditing, Specification API, or QueryDSL.
