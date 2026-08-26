package com.example.P02_EntityLifeCycle.repository;

import com.example.P02_EntityLifeCycle.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
