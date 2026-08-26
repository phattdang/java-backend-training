package com.example.P01_EntityMappingBasic.entity;

import com.example.P01_EntityMappingBasic.enums.UserRole;
import com.example.P01_EntityMappingBasic.enums.UserStatus;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity // Marks this class as a JPA-managed entity.
@Table(name = "users") // Uses an explicit table name instead of the class-name default.
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED) // JPA requires a public or protected no-argument constructor.
public class User {

    @Id // The entity's primary key.
    @GeneratedValue(strategy = GenerationType.IDENTITY) // PostgreSQL generates the value when the row is inserted.
    private Long id;

    @Setter
    @Column(name = "full_name", nullable = false, length = 100)
    private String fullName;

    @Setter
    @Column(nullable = false, unique = true, length = 150)
    private String email;

    @Setter
    @Enumerated(EnumType.STRING) // Stores a readable value such as ACTIVE.
    @Column(nullable = false, length = 20)
    private UserStatus status;

    @Setter
    @Enumerated(EnumType.ORDINAL) // Stores 0, 1, 2...; reordering enum constants can corrupt meaning.
    @Column(nullable = false)
    private UserRole role;

    @Setter
    @Column(name = "date_of_birth")
    private LocalDate dateOfBirth;

    @Column(name = "created_at", nullable = false, updatable = false) // Written once, then omitted from SQL UPDATEs.
    private LocalDateTime createdAt;

    @Setter
    @Column(nullable = false)
    private Boolean active;

    @Setter
    private Integer age;

    @Column(name = "read_only_code", insertable = false) // JPA omits this column from SQL INSERTs.
    private String readOnlyCode;

    @Setter
    @Transient // Exists only in Java memory; no database column is created for it.
    private String temporaryDisplayName;

    public User(String fullName, String email, UserStatus status, UserRole role,
                LocalDate dateOfBirth, Boolean active, Integer age) {
        this.fullName = fullName;
        this.email = email;
        this.status = status;
        this.role = role;
        this.dateOfBirth = dateOfBirth;
        this.createdAt = LocalDateTime.now();
        this.active = active;
        this.age = age;
    }

    // Deliberately no Lombok @Data, @EqualsAndHashCode, or @ToString:
    // generated methods can include mutable IDs and, in later lessons, lazy relationships.
    // That can break hash collections, trigger unexpected queries, or cause recursive output.
}
