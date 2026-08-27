package com.example.P04_EntityRelationships.userprofile.repository;

import com.example.P04_EntityRelationships.userprofile.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
