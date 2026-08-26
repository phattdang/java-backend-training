# Part 2 - Entity Mapping Basics Knowledge Map

Paths in this guide are relative to the `P01_EntityMappingBasic` project root. Annotations imported from `jakarta.persistence` are JPA annotations. Lombok's `@Getter`, `@Setter`, and `@NoArgsConstructor` only reduce boilerplate; they do not define JPA mappings.

| Checklist | Concept | File | Location |
| --- | --- | --- | --- |
| 2.1 | `@Entity` | `User.java` | class annotation |
| 2.2 | `@Table` | `User.java` | class annotation |
| 2.3 | `@Id` | `User.java` | `id` field |
| 2.4 | Primary Key | `User.java` | `id` field |
| 2.5 | `@GeneratedValue` | `User.java` | `id` field |
| 2.6 | `GenerationType.IDENTITY` | `User.java` | `id` field |
| 2.7 | `GenerationType.SEQUENCE` | `SequenceUser.java` | `id` field |
| 2.8 | `GenerationType.AUTO` | `AutoUser.java` | `id` field |
| 2.9 | `@Column` | `User.java` | multiple fields |
| 2.10 | Column name | `User.java` | `fullName` field |
| 2.11 | `nullable` | `User.java` | `fullName`, `email`, and other fields |
| 2.12 | `unique` | `User.java` | `email` field |
| 2.13 | `length` | `User.java` | `fullName`, `email`, `status` fields |
| 2.14 | `insertable / updatable` | `User.java` | `readOnlyCode`, `createdAt` fields |
| 2.15 | Entity field mapping | `User.java` | persistent fields |
| 2.16 | Enum mapping with `@Enumerated` | `User.java` | `status`, `role` fields |
| 2.17 | `STRING` vs `ORDINAL` | `User.java` | `status`, `role` fields |
| 2.18 | Date/Time mapping | `User.java` | `dateOfBirth`, `createdAt` fields |
| 2.19 | `LocalDate` | `User.java` | `dateOfBirth` field |
| 2.20 | `LocalDateTime` | `User.java` | `createdAt` field |
| 2.21 | Boolean / numeric / String | `User.java` | `active`, `age`, `fullName` fields |
| 2.22 | `@Transient` | `User.java` | `temporaryDisplayName` field |
| 2.23 | Entity constructor requirements | `User.java` | constructors/class annotation |
| 2.24 | Whether entities should have setters | `User.java` | selective field annotations |
| 2.25 | Entity `equals()` / `hashCode()` risks | `User.java` | class-end comments (`COMMENT-ONLY EXAMPLE`) |

## 2.1 `@Entity`

**File:** `src/main/java/com/example/P01_EntityMappingBasic/entity/User.java`

**Location:**
```java
@Entity
public class User {
```

**Purpose:** Marks `User` as a JPA-managed entity.

**Database effect:** Hibernate includes it when managing the `users` table.

**How to verify:** Start the application and observe Hibernate creating or updating `users`.

**Important note:** `SequenceUser` and `AutoUser` are also real `@Entity` classes.

## 2.2 `@Table`

**File:** `src/main/java/com/example/P01_EntityMappingBasic/entity/User.java`

**Location:**
```java
@Table(name = "users")
```

**Purpose:** Selects an explicit database table name.

**Database effect:** `User` rows are stored in `users` rather than a provider-chosen default name.

**How to verify:** Inspect the `users` table in PostgreSQL/DBeaver.

**Important note:** `sequence_users` and `auto_users` are explicitly named in their own entities.

## 2.3 `@Id`

**File:** `src/main/java/com/example/P01_EntityMappingBasic/entity/User.java`

**Location:**
```java
@Id
private Long id;
```

**Purpose:** Marks the entity identifier.

**Database effect:** Maps `id` as the identifier column for `users`.

**How to verify:** Inspect the primary-key definition for `users`.

**Important note:** Every entity in this project has one `@Id` field.

## 2.4 Primary Key

**File:** `src/main/java/com/example/P01_EntityMappingBasic/entity/User.java`

**Location:**
```java
@Id
@GeneratedValue(strategy = GenerationType.IDENTITY)
private Long id;
```

**Purpose:** Gives each `User` a stable database identity.

**Database effect:** `users.id` is generated and constrained as the table's primary key.

**How to verify:** Create two users and compare their returned `id` values, then inspect the table constraint.

**Important note:** The Java type and repository ID type both use `Long`.

## 2.5 `@GeneratedValue`

**File:** `src/main/java/com/example/P01_EntityMappingBasic/entity/User.java`

**Location:**
```java
@GeneratedValue(strategy = GenerationType.IDENTITY)
```

**Purpose:** Delegates primary-key value generation instead of requiring application code to set `id`.

**Database effect:** A generated ID is assigned during persistence.

**How to verify:** POST a user without an `id`; the response and database row should contain one.

**Important note:** The separate entities compare three different generation strategies.

## 2.6 `GenerationType.IDENTITY`

**File:** `src/main/java/com/example/P01_EntityMappingBasic/entity/User.java`

**Location:**
```java
@GeneratedValue(strategy = GenerationType.IDENTITY)
```

**Purpose:** Makes the main practice entity use a database identity column.

**Database effect:** PostgreSQL supplies `users.id` when the INSERT occurs.

**How to verify:** Call `POST /practice/users`, inspect the returned ID, and inspect the INSERT SQL.

**Important note:** `User` remains on `IDENTITY`; the other strategies are isolated in other entities.

## 2.7 `GenerationType.SEQUENCE`

**File:** `src/main/java/com/example/P01_EntityMappingBasic/entity/SequenceUser.java`

**Location:**
```java
@SequenceGenerator(
        name = "sequence_user_id_generator",
        sequenceName = "sequence_user_id_sequence",
        allocationSize = 1
)
@GeneratedValue(strategy = GenerationType.SEQUENCE,
        generator = "sequence_user_id_generator")
```

**Purpose:** Demonstrates named sequence-based ID generation separately from `User`.

**Database effect:** IDs for `sequence_users` come from `sequence_user_id_sequence`.

**How to verify:** Call `POST /practice/sequence-users`, inspect the returned ID, and inspect the PostgreSQL sequence.

**Important note:** `allocationSize = 1` makes this training example easy to compare with sequence increments.

## 2.8 `GenerationType.AUTO`

**File:** `src/main/java/com/example/P01_EntityMappingBasic/entity/AutoUser.java`

**Location:**
```java
@GeneratedValue(strategy = GenerationType.AUTO)
```

**Purpose:** Lets the persistence provider select an ID generation strategy.

**Database effect:** Hibernate creates the supporting table/sequence behavior it selects for PostgreSQL.

**How to verify:** Start the application and inspect `auto_users` and the generated schema objects.

**Important note:** This is a real mapped entity, but it intentionally has no repository or REST endpoint.

## 2.9 `@Column`

**File:** `src/main/java/com/example/P01_EntityMappingBasic/entity/User.java`

**Location:**
```java
@Column(name = "full_name", nullable = false, length = 100)
private String fullName;
```

**Purpose:** Customizes how a field maps to a database column.

**Database effect:** Applies the declared name and schema constraints to `full_name`.

**How to verify:** Inspect the generated `users` table definition.

**Important note:** Fields such as `age` demonstrate basic mapping without an explicit `@Column`.

## 2.10 Column name

**File:** `src/main/java/com/example/P01_EntityMappingBasic/entity/User.java`

**Location:**
```java
@Column(name = "full_name")
private String fullName;
```

**Purpose:** Maps the Java field `fullName` to the explicit SQL column `full_name`.

**Database effect:** PostgreSQL uses `full_name` for this value.

**How to verify:** Run `select full_name from users;`.

**Important note:** `dateOfBirth`, `createdAt`, and `readOnlyCode` also declare custom names.

## 2.11 `nullable`

**File:** `src/main/java/com/example/P01_EntityMappingBasic/entity/User.java`

**Location:**
```java
@Column(nullable = false)
private Boolean active;
```

**Purpose:** Declares that a mapped column must not contain SQL `NULL`.

**Database effect:** Hibernate's generated schema marks `active` as not null.

**How to verify:** Inspect the table definition. A request with `active: null` is rejected first by DTO validation; direct SQL can demonstrate the database constraint.

**Important note:** `fullName`, `email`, `status`, `role`, and `createdAt` are also non-nullable.

## 2.12 `unique`

**File:** `src/main/java/com/example/P01_EntityMappingBasic/entity/User.java`

**Location:**
```java
@Column(nullable = false, unique = true, length = 150)
private String email;
```

**Purpose:** Requires email values to be unique.

**Database effect:** Hibernate creates a unique constraint for `users.email`.

**How to verify:** Send the same valid email in two `POST /practice/users` requests; the second insert should violate the database constraint.

**Important note:** Uniqueness is enforced by PostgreSQL, not by a custom repository query.

## 2.13 `length`

**File:** `src/main/java/com/example/P01_EntityMappingBasic/entity/User.java`

**Location:**
```java
@Column(name = "full_name", nullable = false, length = 100)
private String fullName;
```

**Purpose:** Sets the intended maximum size of a String column.

**Database effect:** With schema generation, `full_name` is created with a length of 100.

**How to verify:** Inspect the column type/length in PostgreSQL/DBeaver.

**Important note:** `email` uses 150 and `status` uses 20; DTO validation also checks request lengths where configured.

## 2.14 `insertable / updatable`

**File:** `src/main/java/com/example/P01_EntityMappingBasic/entity/User.java`

**Location:**
```java
@Column(name = "read_only_code", insertable = false)
private String readOnlyCode;

@Column(name = "created_at", nullable = false, updatable = false)
private LocalDateTime createdAt;
```

**Purpose:** Omits `read_only_code` from JPA INSERTs and `created_at` from JPA UPDATEs.

**Database effect:** The respective columns are excluded from the generated SQL operation.

**How to verify:** With SQL logging enabled, inspect INSERT SQL for `read_only_code`. There is currently no update endpoint, so `updatable = false` is verified from mapping/schema behavior or a later manual repository update.

**Important note:** `insertable = false` does not itself generate a database value; `readOnlyCode` remains null unless the database supplies one.

## 2.15 Entity field mapping

**File:** `src/main/java/com/example/P01_EntityMappingBasic/entity/User.java`

**Location:**
```java
@Setter
private Integer age;
```

**Purpose:** Demonstrates that a supported basic field is persistent even without explicit `@Column`.

**Database effect:** JPA maps `age` to a column using its default naming rules.

**How to verify:** Create a user with an age and query `users.age`.

**Important note:** JPA uses field access here because mapping annotations, especially `@Id`, are placed on fields.

## 2.16 Enum mapping with `@Enumerated`

**File:** `src/main/java/com/example/P01_EntityMappingBasic/entity/User.java`

**Location:**
```java
@Enumerated(EnumType.STRING)
private UserStatus status;

@Enumerated(EnumType.ORDINAL)
private UserRole role;
```

**Purpose:** Tells JPA how Java enum values are represented in the database.

**Database effect:** `status` is textual while `role` is numeric.

**How to verify:** POST a user, then query the `status` and `role` columns.

**Important note:** `UserStatus` and `UserRole` are plain Java enums; `@Enumerated` supplies the JPA mapping.

## 2.17 `EnumType.STRING` vs `EnumType.ORDINAL`

**File:** `src/main/java/com/example/P01_EntityMappingBasic/entity/User.java`

**Location:**
```java
@Enumerated(EnumType.STRING)
private UserStatus status;

@Enumerated(EnumType.ORDINAL)
private UserRole role;
```

**Purpose:** Places both enum strategies side by side for comparison.

**Database effect:** A request using `"status": "ACTIVE"` and `"role": "USER"` stores `ACTIVE` and ordinal `0` respectively.

**How to verify:** Run `select status, role from users;` after a normal insert.

**Important note:** Reordering `UserRole` constants changes the meaning of existing ordinal values; `STRING` is generally safer for long-lived data.

## 2.18 Date/Time mapping

**File:** `src/main/java/com/example/P01_EntityMappingBasic/entity/User.java`

**Location:**
```java
private LocalDate dateOfBirth;
private LocalDateTime createdAt;
```

**Purpose:** Demonstrates modern Java date-only and date-time field mappings.

**Database effect:** Hibernate maps these to suitable PostgreSQL date/time columns.

**How to verify:** Create a user and query `date_of_birth` and `created_at`.

**Important note:** No legacy `java.util.Date` or temporal annotation is required for these Java Time types.

## 2.19 `LocalDate`

**File:** `src/main/java/com/example/P01_EntityMappingBasic/entity/User.java`

**Location:**
```java
@Column(name = "date_of_birth")
private LocalDate dateOfBirth;
```

**Purpose:** Stores a calendar date without a time-of-day.

**Database effect:** `date_of_birth` contains a date such as `2000-05-20`.

**How to verify:** Send `"dateOfBirth": "2000-05-20"` and query the stored column.

**Important note:** This field is nullable in the current mapping.

## 2.20 `LocalDateTime`

**File:** `src/main/java/com/example/P01_EntityMappingBasic/entity/User.java`

**Location:**
```java
@Column(name = "created_at", nullable = false, updatable = false)
private LocalDateTime createdAt;
```

**Purpose:** Stores the local date and time when the Java constructor creates the entity.

**Database effect:** `created_at` is inserted and excluded from later JPA UPDATEs.

**How to verify:** POST a user and inspect the returned/stored timestamp.

**Important note:** This is set with `LocalDateTime.now()` in the constructor; JPA auditing is not used.

## 2.21 Boolean / numeric / String mapping

**File:** `src/main/java/com/example/P01_EntityMappingBasic/entity/User.java`

**Location:**
```java
private String fullName;
private Boolean active;
private Integer age;
```

**Purpose:** Demonstrates common basic Java types that JPA maps automatically.

**Database effect:** The values are stored in string, boolean, and integer-compatible PostgreSQL columns.

**How to verify:** POST values for these fields and query the resulting row.

**Important note:** Wrapper types `Boolean` and `Integer` can represent Java `null`; `active` is nevertheless constrained with `nullable = false` and request `@NotNull`.

## 2.22 `@Transient`

**File:** `src/main/java/com/example/P01_EntityMappingBasic/entity/User.java`

**Location:**
```java
@Transient
private String temporaryDisplayName;
```

**Purpose:** Excludes a Java field from persistence.

**Database effect:** No `temporary_display_name` column is created and the value is never stored.

**How to verify:** Supply `temporaryDisplayName` in POST. It appears on that in-memory response but is null after a later GET.

**Important note:** This is JPA's `jakarta.persistence.Transient`, not Java's `transient` keyword.

## 2.23 Entity constructor requirements

**File:** `src/main/java/com/example/P01_EntityMappingBasic/entity/User.java`

**Location:**
```java
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User {

    public User(String fullName, String email, ...) {
```

**Purpose:** Supplies the no-argument constructor JPA needs plus a convenient application constructor.

**Database effect:** No direct schema effect; it allows Hibernate to instantiate loaded entities.

**How to verify:** A successful `GET /practice/users/{id}` shows Hibernate can construct an entity from a row.

**Important note:** `@NoArgsConstructor` is Lombok, not JPA. JPA requires a public or protected no-argument constructor; protected avoids making it part of normal creation code.

## 2.24 Whether entities should have setters

**File:** `src/main/java/com/example/P01_EntityMappingBasic/entity/User.java`

**Location:**
```java
@Setter
private String fullName;

private Long id; // no setter
private LocalDateTime createdAt; // no setter
```

**Purpose:** Demonstrates selective mutability rather than generating setters for every field.

**Database effect:** Setters have no mapping effect by themselves because this entity uses field access; changes to managed persistent fields can still become updates.

**How to verify:** Inspect the generated Lombok methods in the IDE: ordinary mutable fields have setters, while `id`, `createdAt`, and `readOnlyCode` do not.

**Important note:** `@Setter` is Lombok, not JPA. Entities may have setters, but generated IDs and intentionally controlled fields should not be freely replaceable.

## 2.25 Basic risks of Entity `equals()` / `hashCode()`

**Status:** `COMMENT-ONLY EXAMPLE` — the risks are documented; no risky methods are generated or implemented.

**File:** `src/main/java/com/example/P01_EntityMappingBasic/entity/User.java`

**Location:**
```java
// Deliberately no Lombok @Data, @EqualsAndHashCode, or @ToString:
// generated methods can include mutable IDs ...
// That can break hash collections, trigger unexpected queries, or cause recursive output.
```

**Purpose:** Explains why the entity avoids blanket Lombok method generation.

**Database effect:** None.

**How to verify:** Confirm that `User` has no `@Data`, `@EqualsAndHashCode`, or custom equality implementation.

**Important note:** An ID changes from null to generated during persistence, so blindly including it in equality/hash calculations can make hash-based collection behavior unstable.

# File Knowledge Map

## `User.java`

`src/main/java/com/example/P01_EntityMappingBasic/entity/User.java`

- JPA: `@Entity`, `@Table`, `@Id`, `@GeneratedValue(IDENTITY)`, `@Column`, `@Enumerated`, `@Transient`.
- Fields: column naming and constraints, basic types, both enum strategies, `LocalDate`, and `LocalDateTime`.
- Entity design: protected no-argument constructor, application constructor, selective setters, and equality/hash-code risk comments.
- Lombok support: `@Getter`, `@Setter`, and `@NoArgsConstructor`; these reduce boilerplate and are not JPA mappings.

## `SequenceUser.java`

`src/main/java/com/example/P01_EntityMappingBasic/entity/SequenceUser.java`

- JPA: separate `SEQUENCE` ID example using `@SequenceGenerator`.
- Used by `POST /practice/sequence-users`.

## `AutoUser.java`

`src/main/java/com/example/P01_EntityMappingBasic/entity/AutoUser.java`

- JPA: isolated real `AUTO` ID example.
- No repository or endpoint; inspect its generated schema.

## `UserStatus.java` and `UserRole.java`

- `src/main/java/com/example/P01_EntityMappingBasic/enums/UserStatus.java`: constants stored by `User.status` as `STRING`.
- `src/main/java/com/example/P01_EntityMappingBasic/enums/UserRole.java`: constants stored by `User.role` as `ORDINAL`.

## Repository files

- `src/main/java/com/example/P01_EntityMappingBasic/repository/UserRepository.java`: persists and reads `User` for mapping tests.
- `src/main/java/com/example/P01_EntityMappingBasic/repository/SequenceUserRepository.java`: persists `SequenceUser` for sequence testing.
- They contain no custom queries. Repository theory is outside this Part 2 guide.

## REST test path

- `src/main/java/com/example/P01_EntityMappingBasic/dto/CreateUserRequest.java`: validated input fields for `User` creation.
- `src/main/java/com/example/P01_EntityMappingBasic/dto/CreateSequenceUserRequest.java`: validated sequence-user input.
- `src/main/java/com/example/P01_EntityMappingBasic/service/JpaPracticeService.java`: constructs and saves the entities.
- `src/main/java/com/example/P01_EntityMappingBasic/controller/JpaPracticeController.java`: exposes the four manual-test endpoints.
- `PART_2_GUIDE.md`: contains ready-to-send requests and PostgreSQL inspection queries.
