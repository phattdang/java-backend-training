package com.example.P01_EntityMappingBasic.repository;

import com.example.P01_EntityMappingBasic.entity.SequenceUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SequenceUserRepository extends JpaRepository<SequenceUser, Long> {
}
