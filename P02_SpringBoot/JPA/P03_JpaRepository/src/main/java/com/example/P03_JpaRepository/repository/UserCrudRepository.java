package com.example.P03_JpaRepository.repository;

import com.example.P03_JpaRepository.entity.User;
import org.springframework.data.repository.CrudRepository;

/**
 * Learning-only repository showing the basic CRUD abstraction.
 * In a normal Spring Data JPA application, UserRepository (JpaRepository) is usually enough.
 */
public interface UserCrudRepository extends CrudRepository<User, Long> {
}
