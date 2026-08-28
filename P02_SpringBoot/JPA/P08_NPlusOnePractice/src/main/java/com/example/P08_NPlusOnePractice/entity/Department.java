package com.example.P08_NPlusOnePractice.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "departments")
public class Department {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @OneToMany(
            mappedBy = "department",
            fetch = FetchType.LAZY
    )
    private List<Employee> employees = new ArrayList<>();

    public Department(String name) {
        this.name = name;
    }

    public void addEmployee(Employee employee) {
        // This helper only synchronizes the Java object graph.
        // It is unrelated to solving N+1.
        employees.add(employee);
        employee.setDepartment(this);
    }
}
