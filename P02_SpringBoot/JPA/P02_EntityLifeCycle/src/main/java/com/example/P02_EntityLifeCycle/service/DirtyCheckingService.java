package com.example.P02_EntityLifeCycle.service;

import com.example.P02_EntityLifeCycle.dto.DirtyCheckingResult;
import com.example.P02_EntityLifeCycle.entity.User;
import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import static org.springframework.http.HttpStatus.NOT_FOUND;

@Service
public class DirtyCheckingService {

    private final EntityManager entityManager;

    public DirtyCheckingService(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Transactional
    public DirtyCheckingResult demonstrateDirtyChecking(Long id, String newName) {
        User user = entityManager.find(User.class, id);
        if (user == null) {
            throw new ResponseStatusException(NOT_FOUND, "User not found: " + id);
        }

        String oldName = user.getName();
        user.setName(newName);
        // No save(), merge(), or explicit UPDATE: commit flushes the managed change.

        return new DirtyCheckingResult(
                id, oldName, newName, false,
                "Managed + changed + flush/commit causes Hibernate to generate UPDATE."
        );
    }
}
