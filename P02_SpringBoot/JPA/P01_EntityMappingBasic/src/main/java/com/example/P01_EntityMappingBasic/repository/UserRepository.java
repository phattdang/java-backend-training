package com.example.P01_EntityMappingBasic.repository;

import com.example.P01_EntityMappingBasic.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
