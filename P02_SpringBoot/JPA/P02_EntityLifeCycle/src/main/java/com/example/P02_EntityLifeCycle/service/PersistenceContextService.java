package com.example.P02_EntityLifeCycle.service;

import com.example.P02_EntityLifeCycle.dto.LifecycleStateResult;
import com.example.P02_EntityLifeCycle.dto.PersistenceContextResult;
import com.example.P02_EntityLifeCycle.entity.User;
import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import static org.springframework.http.HttpStatus.NOT_FOUND;

@Service
public class PersistenceContextService {

    private final EntityManager entityManager;

    public PersistenceContextService(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Transactional(readOnly = true)
    public LifecycleStateResult demonstrateManagedByPersistenceContext(Long id) {
        User user = requireUser(id);
        boolean managed = entityManager.contains(user);
        return new LifecycleStateResult(
                id, "MANAGED", managed, managed,
                "The current Persistence Context tracks the entity returned by find()."
        );
    }

    @Transactional(readOnly = true)
    public PersistenceContextResult demonstrateFirstLevelCache(Long id) {
        User user1 = requireUser(id);                  // Normally executes one SELECT.
        User user2 = entityManager.find(User.class, id); // Served by the first-level cache.

        return result(id, user1, user2,
                "Inspect SQL: two find() calls in one Persistence Context need only one SELECT.");
    }

    @Transactional(readOnly = true)
    public PersistenceContextResult demonstrateEntityIdentity(Long id) {
        User user1 = requireUser(id);
        User user2 = entityManager.find(User.class, id);

        return result(id, user1, user2,
                "sameJavaInstance is the result of user1 == user2.");
    }

    @Transactional
    public LifecycleStateResult demonstrateClearPersistenceContext(Long id) {
        User user = requireUser(id);
        boolean beforeClear = entityManager.contains(user);

        entityManager.clear(); // Detaches every entity in this Persistence Context.
        boolean afterClear = entityManager.contains(user);

        return new LifecycleStateResult(
                id, "DETACHED_AFTER_CLEAR", beforeClear, afterClear,
                "clear() detached all previously managed entities."
        );
    }

    private PersistenceContextResult result(Long id, User user1, User user2, String observation) {
        return new PersistenceContextResult(
                id,
                entityManager.contains(user1),
                entityManager.contains(user2),
                user1 == user2,
                observation
        );
    }

    private User requireUser(Long id) {
        User user = entityManager.find(User.class, id);
        if (user == null) {
            throw new ResponseStatusException(NOT_FOUND, "User not found: " + id);
        }
        return user;
    }
}
