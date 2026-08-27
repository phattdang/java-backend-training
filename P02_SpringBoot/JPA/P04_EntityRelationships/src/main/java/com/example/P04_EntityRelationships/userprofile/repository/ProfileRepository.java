package com.example.P04_EntityRelationships.userprofile.repository;

import com.example.P04_EntityRelationships.userprofile.entity.Profile;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProfileRepository extends JpaRepository<Profile, Long> {
}
