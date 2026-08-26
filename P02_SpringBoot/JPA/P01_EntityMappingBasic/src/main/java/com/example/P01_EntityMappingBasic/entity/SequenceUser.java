package com.example.P01_EntityMappingBasic.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "sequence_users")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SequenceUser {

    @Id
    @SequenceGenerator(
            name = "sequence_user_id_generator",
            sequenceName = "sequence_user_id_sequence",
            allocationSize = 1
    )
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequence_user_id_generator")
    private Long id;

    @Setter
    @Column(nullable = false, length = 100)
    private String name;

    public SequenceUser(String name) {
        this.name = name;
    }
}
