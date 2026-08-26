package com.example.P01_EntityMappingBasic.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

/** Isolated comparison example; it is intentionally not exposed through a repository or endpoint. */
@Entity
@Table(name = "auto_users")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AutoUser {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO) // The persistence provider selects the database strategy.
    private Long id;

    @Column(nullable = false, length = 100)
    private String name;
}
