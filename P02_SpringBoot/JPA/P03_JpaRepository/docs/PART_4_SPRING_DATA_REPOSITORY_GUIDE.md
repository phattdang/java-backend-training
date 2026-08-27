# Part 4 - Spring Data Repository Guide

## Checklist map

| Checklist | Concept | File | Method / Location |
| --- | --- | --- | --- |
| 4.1 | Repository abstraction | `repository/UserRepository.java` | interface declaration |
| 4.2 | `CrudRepository` | `repository/UserCrudRepository.java` | interface declaration |
| 4.3 | `PagingAndSortingRepository` | `repository/UserPagingRepository.java` | interface declaration |
| 4.4 | `JpaRepository` | `repository/UserRepository.java` | interface declaration |
| 4.5 | Repository differences | `service/RepositoryComparisonService.java` | `compareRepositories()` |
| 4.6 | `save()` | `service/RepositoryCrudService.java` | `saveUser()`, `updateUserWithSave()` |
| 4.7 | `saveAll()` | `service/RepositoryCrudService.java` | `saveAll()` |
| 4.8 | `findById()` | `service/RepositoryCrudService.java` | `findById()`, `findUserOrThrow()` |
| 4.9 | `findAll()` | `service/RepositoryCrudService.java` | `findAll()` |
| 4.10 | `existsById()` | `service/RepositoryCrudService.java` | `existsById()` |
| 4.11 | `count()` | `service/RepositoryCrudService.java` | `count()` |
| 4.12 | `delete()` | `service/RepositoryCrudService.java` | `delete()` |
| 4.13 | `deleteById()` | `service/RepositoryCrudService.java` | `deleteById()` |
| 4.14 | `deleteAll()` | `service/RepositoryCrudService.java` | `deleteAll()` |
| 4.15 | `flush()` | `service/RepositoryFlushService.java` | `flushUpdate()` |
| 4.16 | `saveAndFlush()` | `service/RepositoryFlushService.java` | `saveAndFlush()` |
| 4.17 | `Optional<T>` | `service/RepositoryCrudService.java` | `findById()`, `findUserOrThrow()` |
| 4.18 | Runtime repository implementation | `service/RepositoryComparisonService.java` | `logRuntimeImplementations()`, `compareRepositories()` |

All paths above are relative to:

```text
src/main/java/com/example/P03_JpaRepository/
```

## 4.1 Repository abstraction

**File:** `src/main/java/com/example/P03_JpaRepository/repository/UserRepository.java`

**Location:** interface declaration

**Relevant code:**

```java
public interface UserRepository extends JpaRepository<User, Long> {
}
```

**What it demonstrates:**

A repository exposes persistence operations through an interface. The service depends on this abstraction instead of directly writing `EntityManager` CRUD code.

**How to verify:**

Call `GET /practice/repositories` and inspect the returned runtime class names.

## 4.2 CrudRepository

**File:** `src/main/java/com/example/P03_JpaRepository/repository/UserCrudRepository.java`

**Location:** interface declaration

**Relevant code:**

```java
public interface UserCrudRepository extends CrudRepository<User, Long> {
}
```

**What it demonstrates:**

`CrudRepository` is the basic repository abstraction for saving, finding, counting, and deleting entities.

**How to verify:**

Call `GET /practice/repositories`. This project intentionally does not use this learning-only interface for the main CRUD service.

## 4.3 PagingAndSortingRepository

**File:** `src/main/java/com/example/P03_JpaRepository/repository/UserPagingRepository.java`

**Location:** interface declaration

**Relevant code:**

```java
public interface UserPagingRepository
        extends PagingAndSortingRepository<User, Long> {
}
```

**What it demonstrates:**

`PagingAndSortingRepository` exposes the paging and sorting abstraction. Part 4 only compares the interface; it does not execute pagination or sorting.

**How to verify:**

Call `GET /practice/repositories` and inspect its purpose and runtime proxy class.

## 4.4 JpaRepository

**File:** `src/main/java/com/example/P03_JpaRepository/repository/UserRepository.java`

**Location:** interface declaration

**Relevant code:**

```java
public interface UserRepository extends JpaRepository<User, Long> {
}
```

**What it demonstrates:**

`JpaRepository` provides broad repository functionality and JPA-specific operations such as `flush()` and `saveAndFlush()`. In normal Spring Data JPA projects, this is commonly the only repository base interface needed.

**How to verify:**

Use any `/practice/users` endpoint, then call `GET /practice/repositories`.

## 4.5 Differences between repository interfaces

**File:** `src/main/java/com/example/P03_JpaRepository/service/RepositoryComparisonService.java`

**Method:** `compareRepositories()`

**Relevant code:**

```java
comparison.put("CrudRepository", ...);
comparison.put("PagingAndSortingRepository", ...);
comparison.put("JpaRepository", ...);
```

**What it demonstrates:**

- `CrudRepository`: CRUD basics.
- `PagingAndSortingRepository`: paging and sorting abstraction.
- `JpaRepository`: broader JPA-focused functionality, including flush operations.

The three interfaces are separate here for learning visibility. `JpaRepository` is usually enough in a normal Spring Data JPA project.

**How to verify:**

Call `GET /practice/repositories`.

## 4.6 save()

**Files:**

- `src/main/java/com/example/P03_JpaRepository/controller/RepositoryPracticeController.java`
- `src/main/java/com/example/P03_JpaRepository/service/RepositoryCrudService.java`

**Methods:** `saveUser(...)`, `updateUserWithSave(...)`

**Relevant code:**

```java
userRepository.save(newUser);
userRepository.save(existingUser);
```

**What it demonstrates:**

Spring Data JPA determines whether an entity is new. A new entity gets persist-like behavior; a non-new entity gets merge-like behavior when appropriate. `save()` should not be understood as always issuing one specific SQL operation.

**How to verify:**

Call `POST /practice/users`, then `PUT /practice/users/{id}` and compare the SQL logs.

## 4.7 saveAll()

**File:** `src/main/java/com/example/P03_JpaRepository/service/RepositoryCrudService.java`

**Method:** `saveAll(...)`

**Relevant code:**

```java
userRepository.saveAll(users)
```

**What it demonstrates:**

`saveAll()` accepts multiple entities through one repository call. This example demonstrates repository API behavior only; it does not configure JDBC batch optimization.

**How to verify:**

Call `POST /practice/users/batch` with a JSON array containing unique email addresses and inspect the SQL log.

## 4.8 findById()

**File:** `src/main/java/com/example/P03_JpaRepository/service/RepositoryCrudService.java`

**Methods:** `findById(...)`, `findUserOrThrow(...)`

**Relevant code:**

```java
userRepository.findById(id)
```

**What it demonstrates:**

`findById()` looks up an entity by its primary key and returns `Optional<User>`.

**How to verify:**

Call `GET /practice/users/{id}` and inspect the select statement in the SQL log.

## 4.9 findAll()

**File:** `src/main/java/com/example/P03_JpaRepository/service/RepositoryCrudService.java`

**Method:** `findAll()`

**Relevant code:**

```java
userRepository.findAll()
```

**What it demonstrates:**

The `JpaRepository` version returns all `User` entities as a `List<User>`. Pagination is deliberately not introduced here.

**How to verify:**

Call `GET /practice/users`.

## 4.10 existsById()

**File:** `src/main/java/com/example/P03_JpaRepository/service/RepositoryCrudService.java`

**Method:** `existsById(...)`

**Relevant code:**

```java
userRepository.existsById(id)
```

**What it demonstrates:**

Checks whether a row exists for a primary key and returns a boolean.

**How to verify:**

Call `GET /practice/users/{id}/exists` with an existing and a missing ID.

## 4.11 count()

**File:** `src/main/java/com/example/P03_JpaRepository/service/RepositoryCrudService.java`

**Method:** `count()`

**Relevant code:**

```java
userRepository.count()
```

**What it demonstrates:**

Returns the number of `User` rows.

**How to verify:**

Call `GET /practice/users/count`, create a user, and call it again.

## 4.12 delete()

**Files:**

- `src/main/java/com/example/P03_JpaRepository/controller/RepositoryPracticeController.java`
- `src/main/java/com/example/P03_JpaRepository/service/RepositoryCrudService.java`

**Methods:** `findUserOrThrow(...)`, `delete(...)`

**Relevant code:**

```java
crudService.delete(crudService.findUserOrThrow(id));
userRepository.delete(user);
```

**What it demonstrates:**

The endpoint loads a `User` first and then passes the entity instance to `delete()`.

**How to verify:**

Create a disposable user, call `DELETE /practice/users/entity/{id}`, and inspect the select/delete SQL.

## 4.13 deleteById()

**File:** `src/main/java/com/example/P03_JpaRepository/service/RepositoryCrudService.java`

**Method:** `deleteById(...)`

**Relevant code:**

```java
userRepository.deleteById(id)
```

**What it demonstrates:**

Deletes through the repository API using the entity ID.

**How to verify:**

Create a disposable user, call `DELETE /practice/users/{id}`, and inspect the SQL log.

## 4.14 deleteAll()

**File:** `src/main/java/com/example/P03_JpaRepository/service/RepositoryCrudService.java`

**Method:** `deleteAll()`

**Relevant code:**

```java
userRepository.deleteAll()
```

**What it demonstrates:**

Removes every `User` entity. This destructive endpoint exists for learning only.

**How to verify:**

Call `DELETE /practice/users`, then `GET /practice/users/count`. Do not use this endpoint against important data.

## 4.15 flush()

**File:** `src/main/java/com/example/P03_JpaRepository/service/RepositoryFlushService.java`

**Method:** `flushUpdate(...)`

**Relevant code:**

```java
user.setName(request.name());
userRepository.flush();
```

**What it demonstrates:**

Inside a transaction, a managed entity is modified and `flush()` forces pending SQL to synchronize with the database before the method returns. The transaction is still active; flushing does not mean committing.

**How to verify:**

Call `POST /practice/users/{id}/flush`. Compare the service's before/after messages with the Hibernate SQL output.

## 4.16 saveAndFlush()

**File:** `src/main/java/com/example/P03_JpaRepository/service/RepositoryFlushService.java`

**Method:** `saveAndFlush(...)`

**Relevant code:**

```java
userRepository.saveAndFlush(user)
```

**What it demonstrates:**

`saveAndFlush()` saves and immediately synchronizes pending SQL. It does not commit the active transaction.

**How to verify:**

Call `POST /practice/users/save-and-flush` and compare the service messages with the insert SQL.

## 4.17 Optional<T>

**File:** `src/main/java/com/example/P03_JpaRepository/service/RepositoryCrudService.java`

**Methods:** `findById(...)`, `findUserOrThrow(...)`

**Relevant code:**

```java
Optional<User> optionalUser = userRepository.findById(id);
User user = optionalUser.orElseThrow(() -> userNotFound(id));
```

**What it demonstrates:**

`Optional<User>` explicitly represents either a found user or no value. `orElseThrow()` converts the empty case into an HTTP 404 response.

**How to verify:**

Call `GET /practice/users/{id}` first with an existing ID and then with a missing ID.

## 4.18 Repository implementation created by Spring

**File:** `src/main/java/com/example/P03_JpaRepository/service/RepositoryComparisonService.java`

**Methods:** `logRuntimeImplementations()`, `compareRepositories()`

**Relevant code:**

```java
jpaRepository.getClass().getName()
```

**What it demonstrates:**

```text
UserRepository interface
-> Spring Data scans it
-> Spring creates a runtime proxy/implementation
-> Spring registers that object as a bean
-> RepositoryComparisonService injects the bean
-> the generated implementation uses JPA/EntityManager underneath
```

There is intentionally no `UserRepositoryImpl` for CRUD operations.

**How to verify:**

Start the application and inspect the three runtime-class log messages, or call `GET /practice/repositories`.

## Suggested experiment order

1. `GET /practice/repositories`
2. `POST /practice/users`
3. `POST /practice/users/batch`
4. `GET /practice/users/{id}`
5. `GET /practice/users`
6. `GET /practice/users/{id}/exists`
7. `GET /practice/users/count`
8. `PUT /practice/users/{id}`
9. `POST /practice/users/{id}/flush`
10. `POST /practice/users/save-and-flush`
11. Run the three delete experiments only with disposable data.

# File -> Knowledge Map

```text
User.java
-> simple JPA entity used by repository operations

UserCrudRepository.java
-> CrudRepository

UserPagingRepository.java
-> PagingAndSortingRepository abstraction only

UserRepository.java
-> JpaRepository
-> repository used by the CRUD and flush services

RepositoryCrudService.java
-> save
-> saveAll
-> findById
-> findAll
-> existsById
-> count
-> delete
-> deleteById
-> deleteAll
-> Optional<T>

RepositoryFlushService.java
-> flush
-> saveAndFlush
-> flush is not commit

RepositoryComparisonService.java
-> repository-interface comparison
-> Spring-created runtime proxy

RepositoryPracticeController.java
-> API index
-> every endpoint comment points to its Part 4 checklist item and service method

PART_4_SPRING_DATA_REPOSITORY_GUIDE.md
-> complete checklist-to-code knowledge map
```

## Part 4 boundary

This project does not use derived query methods, custom JPQL, native SQL, relationships, projections, auditing, locking, pagination execution, or sorting execution. Those topics belong to later parts.
