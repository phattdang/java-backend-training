package com.example.P06_FetchingLazyEager.eagerdemo.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@Table(name = "eager_employees")
@Getter
@Setter
@NoArgsConstructor
public class EagerEmployee {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(nullable = false, unique = true, length = 150)
    private String email;

    @Column(nullable = false, precision = 19, scale = 2)
    private BigDecimal salary;

    // Isolated EAGER mapping. EAGER guarantees fetched state, not a particular SQL shape.
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "department_id", nullable = false)
    private EagerDepartment department;

    public EagerEmployee(
            String name,
            String email,
            BigDecimal salary,
            EagerDepartment department) {
        this.name = name;
        this.email = email;
        this.salary = salary;
        this.department = department;
    }
}
