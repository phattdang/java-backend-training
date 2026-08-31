package com.example.P01_SpringSecurityArchitecture.entity;

import com.example.P01_SpringSecurityArchitecture.entity.enums.Role;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@Table(name = "users")
@ToString
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    String email;

    String password;

    @Enumerated(value = EnumType.STRING)
    Role role;

    boolean enabled = true;

    boolean locked = false;
}
