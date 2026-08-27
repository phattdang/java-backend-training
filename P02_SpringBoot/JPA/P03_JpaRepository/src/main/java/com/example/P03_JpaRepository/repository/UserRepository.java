package com.example.P03_JpaRepository.repository;

import com.example.P03_JpaRepository.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * The repository used by the practice services. JpaRepository combines broad repository
 * functionality with JPA-specific operations such as flush() and saveAndFlush().
 */
public interface UserRepository extends JpaRepository<User, Long> {
}
