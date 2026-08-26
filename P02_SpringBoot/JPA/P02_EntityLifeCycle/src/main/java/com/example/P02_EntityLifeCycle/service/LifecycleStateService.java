package com.example.P02_EntityLifeCycle.service;

import com.example.P02_EntityLifeCycle.dto.LifecycleStateResult;
import com.example.P02_EntityLifeCycle.entity.User;
import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import static org.springframework.http.HttpStatus.NOT_FOUND;

@Service
public class LifecycleStateService {

    private final EntityManager entityManager;

    public LifecycleStateService(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Transactional(readOnly = true)
    public LifecycleStateResult demonstrateTransientState() {
        User user = new User("Transient example", "not-persisted@example.com");
        // New Java object: it has no database identity and is not managed.
        boolean contained = entityManager.contains(user);
        return new LifecycleStateResult(
                user.getId(), "TRANSIENT", contained, contained,
                "No INSERT occurs because persist/save is never called."
        );
    }

    @Transactional(readOnly = true)
    public LifecycleStateResult demonstrateManagedState(Long id) {
        User user = requireUser(id);
        // find() associates the returned entity with this Persistence Context.
        boolean contained = entityManager.contains(user);
        return new LifecycleStateResult(
                user.getId(), "MANAGED", contained, contained,
                "entityManager.contains(user) is true inside this transaction."
        );
    }

    @Transactional
    public LifecycleStateResult demonstrateDetachedState(Long id, String detachedName) {
        User user = requireUser(id);
        boolean beforeDetach = entityManager.contains(user);

        entityManager.detach(user); // Managed -> Detached.
        boolean afterDetach = entityManager.contains(user);
        user.setName(detachedName); // This change is not dirty-checked.

        return new LifecycleStateResult(
                user.getId(), "DETACHED", beforeDetach, afterDetach,
                "The Java name changed, but no UPDATE is generated at commit."
        );
    }

    @Transactional
    public LifecycleStateResult demonstrateRemovedState(Long id) {
        User user = requireUser(id);
        boolean beforeRemove = entityManager.contains(user);

        entityManager.remove(user); // Managed -> Removed; DELETE occurs on flush/commit.
        boolean afterRemove = entityManager.contains(user);

        return new LifecycleStateResult(
                user.getId(), "REMOVED", beforeRemove, afterRemove,
                "The DELETE is synchronized on flush or transaction commit."
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
