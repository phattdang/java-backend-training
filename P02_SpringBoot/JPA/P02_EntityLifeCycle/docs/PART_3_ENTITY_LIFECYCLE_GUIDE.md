# Part 3 - Entity Lifecycle & Persistence Context Knowledge Map

Paths are relative to the `P02_EntityLifeCycle` project root. Each service method has its own transaction boundary so the Persistence Context behavior is isolated and repeatable. SQL logging is enabled in `application.properties`.

| Checklist | Concept | File | Method / Location |
| --- | --- | --- | --- |
| 3.1 | Entity lifecycle | `LifecycleStateService.java` | four lifecycle demo methods |
| 3.2 | Transient state | `LifecycleStateService.java` | `demonstrateTransientState()` |
| 3.3 | Managed / Persistent state | `LifecycleStateService.java` | `demonstrateManagedState()` |
| 3.4 | Detached state | `LifecycleStateService.java` | `demonstrateDetachedState()` |
| 3.5 | Removed state | `LifecycleStateService.java` | `demonstrateRemovedState()` |
| 3.6 | Persistence Context manages entities | `PersistenceContextService.java` | `demonstrateManagedByPersistenceContext()` |
| 3.7 | First-Level Cache | `PersistenceContextService.java` | `demonstrateFirstLevelCache()` |
| 3.8 | Entity identity | `PersistenceContextService.java` | `demonstrateEntityIdentity()` |
| 3.9 | Dirty Checking | `DirtyCheckingService.java` | `demonstrateDirtyChecking()` |
| 3.10 | Automatic UPDATE | `DirtyCheckingService.java` | `demonstrateDirtyChecking()` |
| 3.11 | Flush | `FlushService.java` | `demonstrateFlush()` |
| 3.12 | `flush()` vs commit | `FlushService.java` | `demonstrateFlush()` and log messages |
| 3.13 | Clear Persistence Context | `PersistenceContextService.java` | `demonstrateClearPersistenceContext()` |
| 3.14 | Detach Entity | `LifecycleStateService.java` | `demonstrateDetachedState()` |
| 3.15 | `persist()` | `EntityManagerOperationService.java` | `demonstratePersist()` |
| 3.16 | `merge()` | `EntityManagerOperationService.java` | `demonstrateMerge()` |
| 3.17 | `remove()` | `EntityManagerOperationService.java` | `demonstrateRemove()` |
| 3.18 | `find()` | `EntityManagerOperationService.java` | `demonstrateFind()` |
| 3.19 | `getReference()` | `EntityManagerOperationService.java` | `demonstrateGetReference()` |
| 3.20 | EntityManager inside Spring Data JPA | `EntityManagerOperationService.java` | `compareRepositoryAndEntityManager()` |

## Before running the experiments

Create the PostgreSQL database if it is not already present:

```sql
create database jpa_lifecycle_practice;
```

The application runs on port `8083`. Set `JPA_LIFECYCLE_DB_USERNAME` and `JPA_LIFECYCLE_DB_PASSWORD` when the local values differ from `postgres`.

Create a reusable row first:

```http
POST /practice/users
Content-Type: application/json

{
  "name": "Lifecycle User",
  "email": "lifecycle@example.com"
}
```

Use the returned ID in later requests. Removal demos delete their target row, so create another user before each removal experiment.

## 3.1 Entity lifecycle

**File:** `src/main/java/com/example/P02_EntityLifeCycle/service/LifecycleStateService.java`

**Methods:** `demonstrateTransientState()`, `demonstrateManagedState(...)`, `demonstrateDetachedState(...)`, `demonstrateRemovedState(...)`

**Important code:**

```java
new User(...);
entityManager.find(User.class, id);
entityManager.detach(user);
entityManager.remove(user);
```

**What this demonstrates:** The four states are represented by separate methods instead of one long scenario.

**What to observe:** Each response names the state and reports `entityManager.contains(...)` before and after the important operation.

**Important note:** Lifecycle state is relative to the current Persistence Context. A later request normally uses a new transaction-scoped context.

## 3.2 Transient state

**File:** `src/main/java/com/example/P02_EntityLifeCycle/service/LifecycleStateService.java`

**Method:** `demonstrateTransientState()`

**Important code:**

```java
User user = new User("Transient example", "not-persisted@example.com");
boolean contained = entityManager.contains(user);
```

**What this demonstrates:** A newly constructed entity has no generated ID and is not managed.

**What to observe:** `POST /practice/lifecycle/transient` returns state `TRANSIENT`, null ID, and `contained...: false`. No INSERT appears.

**Important note:** Constructing an entity alone never persists it.

## 3.3 Managed / Persistent state

**File:** `src/main/java/com/example/P02_EntityLifeCycle/service/LifecycleStateService.java`

**Method:** `demonstrateManagedState(Long id)`

**Important code:**

```java
User user = entityManager.find(User.class, id);
boolean contained = entityManager.contains(user);
```

**What this demonstrates:** An entity loaded into the current Persistence Context is managed.

**What to observe:** `POST /practice/lifecycle/managed/{id}` returns `MANAGED` and `contained...: true`.

**Important note:** “Managed” and “persistent” describe the same lifecycle state here.

## 3.4 Detached state

**File:** `src/main/java/com/example/P02_EntityLifeCycle/service/LifecycleStateService.java`

**Method:** `demonstrateDetachedState(Long id, String detachedName)`

**Important code:**

```java
entityManager.detach(user);
user.setName(detachedName);
```

**What this demonstrates:** The Java object still exists after detachment, but the Persistence Context no longer tracks it.

**What to observe:** POST `/practice/lifecycle/detached/{id}` with `{"name":"Detached Name"}`. The result changes from contained `true` to `false`; no UPDATE appears and a later GET retains the database name.

**Important note:** Changing a detached instance is not dirty-checked.

## 3.5 Removed state

**File:** `src/main/java/com/example/P02_EntityLifeCycle/service/LifecycleStateService.java`

**Method:** `demonstrateRemovedState(Long id)`

**Important code:**

```java
entityManager.remove(user);
```

**What this demonstrates:** A managed entity becomes removed and is scheduled for deletion.

**What to observe:** `POST /practice/lifecycle/removed/{id}` reports `REMOVED`; Hibernate emits DELETE by flush/commit and a later GET returns 404.

**Important note:** The row is not merely hidden—the successful transaction deletes it.

## 3.6 How Persistence Context manages entities

**File:** `src/main/java/com/example/P02_EntityLifeCycle/service/PersistenceContextService.java`

**Method:** `demonstrateManagedByPersistenceContext(Long id)`

**Important code:**

```java
User user = entityManager.find(User.class, id);
boolean managed = entityManager.contains(user);
```

**What this demonstrates:** The current Persistence Context tracks an entity loaded through its EntityManager.

**What to observe:** `GET /practice/context/managed/{id}` returns `contained...: true`.

**Important note:** `@Transactional` supplies a clear transaction-scoped context for each service experiment.

## 3.7 First-Level Cache

**File:** `src/main/java/com/example/P02_EntityLifeCycle/service/PersistenceContextService.java`

**Method:** `demonstrateFirstLevelCache(Long id)`

**Important code:**

```java
User user1 = entityManager.find(User.class, id);
User user2 = entityManager.find(User.class, id);
```

**What this demonstrates:** The Persistence Context remembers the managed entity by type and ID.

**What to observe:** `GET /practice/context/first-level-cache/{id}` performs two `find()` calls but normally logs only one SELECT.

**Important note:** The cache belongs to this Persistence Context; separate HTTP requests do not demonstrate the same first-level cache hit.

## 3.8 Entity identity inside Persistence Context

**File:** `src/main/java/com/example/P02_EntityLifeCycle/service/PersistenceContextService.java`

**Method:** `demonstrateEntityIdentity(Long id)`

**Important code:**

```java
boolean sameJavaInstance = user1 == user2;
```

**What this demonstrates:** The same entity type and ID resolve to the same Java object within one Persistence Context.

**What to observe:** `GET /practice/context/identity/{id}` returns `sameJavaInstance: true`.

**Important note:** This is reference identity, not an `equals()`/`hashCode()` experiment.

## 3.9 Dirty Checking

**File:** `src/main/java/com/example/P02_EntityLifeCycle/service/DirtyCheckingService.java`

**Method:** `demonstrateDirtyChecking(Long id, String newName)`

**Important code:**

```java
User user = entityManager.find(User.class, id);
user.setName(newName);
```

**What this demonstrates:** Hibernate tracks changes made to a managed entity.

**What to observe:** PUT `/practice/dirty-checking/{id}` with `{"name":"Dirty Checked"}`. An UPDATE appears even though the response confirms no save or merge was called.

**Important note:** The method must run in a transaction so synchronization can occur at successful completion.

## 3.10 When automatic UPDATE is generated

**File:** `src/main/java/com/example/P02_EntityLifeCycle/service/DirtyCheckingService.java`

**Method:** `demonstrateDirtyChecking(Long id, String newName)`

**Important code:**

```java
// No save(), merge(), or explicit UPDATE: commit flushes the managed change.
```

**What this demonstrates:** Automatic UPDATE requires: managed entity + changed state + dirty checking + flush/commit.

**What to observe:** The UPDATE is logged near transaction completion, after the Java setter executes.

**Important note:** Setting the same value may produce no UPDATE because there is no effective dirty change.

## 3.11 Flush

**File:** `src/main/java/com/example/P02_EntityLifeCycle/service/FlushService.java`

**Method:** `demonstrateFlush(Long id, String newName)`

**Important code:**

```java
user.setName(newName);
entityManager.flush();
```

**What this demonstrates:** Explicit flush synchronizes pending Persistence Context changes to the database immediately.

**What to observe:** POST `/practice/flush/{id}` with a new name. The UPDATE appears between the service's “Before” and “After” flush log messages.

**Important note:** With `IDENTITY`, an INSERT used to obtain a generated ID may occur during `persist()`; flush timing is easiest to observe here with UPDATE.

## 3.12 `flush()` vs commit

**File:** `src/main/java/com/example/P02_EntityLifeCycle/service/FlushService.java`

**Method:** `demonstrateFlush(Long id, String newName)`

**Important code:**

```java
entityManager.flush(); // Synchronizes changes now; it does NOT commit the transaction.
```

**What this demonstrates:** Flush sends synchronized SQL while commit successfully completes the transaction.

**What to observe:** The response reports `flushCalled: true` and `flushCommitsTransaction: false`; logs show SQL during the method, while Spring commits after successful method return.

**Important note:** Flushed SQL can still be rolled back if the transaction later fails.

## 3.13 Clear Persistence Context

**File:** `src/main/java/com/example/P02_EntityLifeCycle/service/PersistenceContextService.java`

**Method:** `demonstrateClearPersistenceContext(Long id)`

**Important code:**

```java
entityManager.clear();
boolean afterClear = entityManager.contains(user);
```

**What this demonstrates:** `clear()` detaches every managed entity from the current Persistence Context.

**What to observe:** `POST /practice/context/clear/{id}` changes contained from `true` to `false`.

**Important note:** This method makes no pending field change before clearing, so no data is intentionally discarded.

## 3.14 Detach Entity

**File:** `src/main/java/com/example/P02_EntityLifeCycle/service/LifecycleStateService.java`

**Method:** `demonstrateDetachedState(Long id, String detachedName)`

**Important code:**

```java
entityManager.detach(user);
boolean afterDetach = entityManager.contains(user);
```

**What this demonstrates:** `detach()` removes one specific entity from management, unlike `clear()` which detaches all.

**What to observe:** The endpoint response reports `containedBeforeOperation: true` and `containedAfterOperation: false`; its subsequent name change produces no UPDATE.

**Important note:** The detached object remains a normal usable Java object.

## 3.15 `persist()`

**File:** `src/main/java/com/example/P02_EntityLifeCycle/service/EntityManagerOperationService.java`

**Method:** `demonstratePersist(CreateUserRequest request)`

**Important code:**

```java
User user = new User(request.name(), request.email());
entityManager.persist(user);
```

**What this demonstrates:** `persist()` changes the same entity instance from transient to managed.

**What to observe:** POST `/practice/entity-manager/persist`; the response changes managed status from `false` to `true` and includes a generated ID.

**Important note:** This is direct EntityManager usage, not `JpaRepository.save()`.

## 3.16 `merge()`

**File:** `src/main/java/com/example/P02_EntityLifeCycle/service/EntityManagerOperationService.java`

**Method:** `demonstrateMerge(Long id, String newName)`

**Important code:**

```java
entityManager.detach(detachedUser);
User managed = entityManager.merge(detachedUser);
```

**What this demonstrates:** `merge()` copies detached state into and returns a managed instance.

**What to observe:** POST `/practice/entity-manager/merge/{id}` with a new name. The original reports unmanaged, returned instance reports managed, `sameJavaInstance` is false, and UPDATE occurs.

**Important note:** The original detached instance does not become managed; continue working with the value returned by `merge()`.

## 3.17 `remove()`

**File:** `src/main/java/com/example/P02_EntityLifeCycle/service/EntityManagerOperationService.java`

**Method:** `demonstrateRemove(Long id)`

**Important code:**

```java
User user = entityManager.find(User.class, id);
entityManager.remove(user);
```

**What this demonstrates:** `remove()` accepts a managed entity and schedules its row for deletion.

**What to observe:** `POST /practice/entity-manager/remove/{id}` produces DELETE on flush/commit.

**Important note:** Create a disposable user for this destructive practice endpoint.

## 3.18 `find()`

**File:** `src/main/java/com/example/P02_EntityLifeCycle/service/EntityManagerOperationService.java`

**Method:** `demonstrateFind(Long id)`

**Important code:**

```java
User user = entityManager.find(User.class, id);
```

**What this demonstrates:** `find()` loads by entity type and primary key and returns null when no row exists.

**What to observe:** `GET /practice/entity-manager/find/{id}` returns `found: true` and `managed: true`; an unknown ID returns `found: false` rather than 404.

**Important note:** This endpoint deliberately exposes the null-not-found behavior.

## 3.19 `getReference()`

**File:** `src/main/java/com/example/P02_EntityLifeCycle/service/EntityManagerOperationService.java`

**Method:** `demonstrateGetReference(Long id)`

**Important code:**

```java
User reference = entityManager.getReference(User.class, id);
Long referenceId = reference.getId();
```

**What this demonstrates:** JPA may return a managed reference/proxy-like object without immediately loading all entity data.

**What to observe:** `GET /practice/entity-manager/reference/{id}` returns its runtime class and ID. Compare SQL logs with the `find()` endpoint.

**Important note:** The demo intentionally reads only the ID. Accessing other state can require database data, and a missing row may be reported when that state is accessed.

## 3.20 EntityManager inside Spring Data JPA

**File:** `src/main/java/com/example/P02_EntityLifeCycle/service/EntityManagerOperationService.java`

**Method:** `compareRepositoryAndEntityManager(Long id)`

**Important code:**

```java
User repositoryUser = userRepository.findById(id).orElseThrow(...);
User entityManagerUser = entityManager.find(User.class, id);
```

**What this demonstrates:** A Spring Data JPA repository operates on top of JPA and participates in the same transaction-scoped Persistence Context as the injected EntityManager.

**What to observe:** `GET /practice/entity-manager/compare/{id}` reports both instances managed and `sameJavaInstance: true`; normally only the first lookup needs SELECT.

**Important note:** `UserRepository` remains a simple `JpaRepository<User, Long>` helper; repository implementation details are not part of this project.

# File  Knowledge Map

## `LifecycleStateService.java`

- Transient, managed, detached, and removed states.
- `entityManager.contains(...)` before/after state transitions.
- A detached-object change that is deliberately not persisted.

## `PersistenceContextService.java`

- Persistence Context management.
- First-level cache and entity reference identity.
- `clear()` and its detach-all effect.

## `DirtyCheckingService.java`

- Dirty checking.
- Automatic UPDATE without `save()` or `merge()`.

## `FlushService.java`

- Explicit `flush()`.
- Log markers showing SQL synchronization before method completion.
- Flush versus commit distinction.

## `EntityManagerOperationService.java`

- Direct `persist()`, `merge()`, `remove()`, `find()`, and `getReference()` demos.
- Repository-versus-EntityManager comparison in one Persistence Context.
- Simple repository-based create/read helpers for preparing experiments.

## `User.java`

- Minimal lifecycle entity: generated `id`, `name`, and `email` only.
- No relationships or lifecycle callbacks.

## `UserRepository.java`

- `JpaRepository<User, Long>` used only to prepare/read data and compare with EntityManager.
- No custom queries.

## Controller and DTO files

- `JpaLifecyclePracticeController.java` gives every experiment an independent endpoint.
- `CreateUserRequest.java` and `UpdateUserNameRequest.java` validate inputs.
- Result records expose lifecycle checks without returning proxy/reference objects directly.

## `application.properties`

- PostgreSQL database: `jpa_lifecycle_practice`.
- Port: `8083`.
- `ddl-auto=update`, SQL display, and formatted Hibernate SQL are enabled.
- Open EntityManager in View is disabled so the service transaction boundaries remain clear.
