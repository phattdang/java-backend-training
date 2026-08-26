package com.example.P02_EntityLifeCycle.service;

import com.example.P02_EntityLifeCycle.dto.CreateUserRequest;
import com.example.P02_EntityLifeCycle.dto.EntityManagerOperationResult;
import com.example.P02_EntityLifeCycle.dto.FindResult;
import com.example.P02_EntityLifeCycle.dto.PersistenceContextResult;
import com.example.P02_EntityLifeCycle.dto.ReferenceResult;
import com.example.P02_EntityLifeCycle.entity.User;
import com.example.P02_EntityLifeCycle.repository.UserRepository;
import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import static org.springframework.http.HttpStatus.NOT_FOUND;

@Service
public class EntityManagerOperationService {

    private final EntityManager entityManager;
    private final UserRepository userRepository;

    public EntityManagerOperationService(EntityManager entityManager, UserRepository userRepository) {
        this.entityManager = entityManager;
        this.userRepository = userRepository;
    }

    public User createUsingRepository(CreateUserRequest request) {
        return userRepository.save(new User(request.name(), request.email()));
    }

    public User findUsingRepository(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(NOT_FOUND, "User not found: " + id));
    }

    @Transactional
    public EntityManagerOperationResult demonstratePersist(CreateUserRequest request) {
        User user = new User(request.name(), request.email());
        boolean beforePersist = entityManager.contains(user);

        entityManager.persist(user); // Transient -> Managed.
        boolean afterPersist = entityManager.contains(user);

        return new EntityManagerOperationResult(
                user.getId(), "PERSIST", beforePersist, afterPersist, true,
                "persist() made the same Java instance managed."
        );
    }

    @Transactional
    public EntityManagerOperationResult demonstrateMerge(Long id, String newName) {
        User detachedUser = requireUser(id);
        entityManager.detach(detachedUser);
        detachedUser.setName(newName);

        User managed = entityManager.merge(detachedUser);
        // merge() returns a managed copy; the original object stays detached.
        return new EntityManagerOperationResult(
                managed.getId(), "MERGE",
                entityManager.contains(detachedUser),
                entityManager.contains(managed),
                detachedUser == managed,
                "Original remains detached; the returned instance is managed and is updated at flush/commit."
        );
    }

    @Transactional
    public EntityManagerOperationResult demonstrateRemove(Long id) {
        User user = requireUser(id);
        boolean beforeRemove = entityManager.contains(user);
        entityManager.remove(user);

        return new EntityManagerOperationResult(
                id, "REMOVE", beforeRemove, entityManager.contains(user), true,
                "remove() marks the managed entity for DELETE on flush/commit."
        );
    }

    @Transactional(readOnly = true)
    public FindResult demonstrateFind(Long id) {
        User user = entityManager.find(User.class, id);
        if (user == null) {
            return new FindResult(id, false, null, false,
                    "find() returns null when the row does not exist.");
        }
        return new FindResult(id, true, user.getName(), entityManager.contains(user),
                "find() returned a managed entity.");
    }

    @Transactional(readOnly = true)
    public ReferenceResult demonstrateGetReference(Long id) {
        User reference = entityManager.getReference(User.class, id);
        // Reading only the identifier avoids intentionally forcing full initialization here.
        return new ReferenceResult(
                id,
                reference.getId(),
                reference.getClass().getName(),
                entityManager.contains(reference),
                "Obtaining the reference may avoid an immediate SELECT; accessing other state may require data."
        );
    }

    @Transactional(readOnly = true)
    public PersistenceContextResult compareRepositoryAndEntityManager(Long id) {
        User repositoryUser = userRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(NOT_FOUND, "User not found: " + id));
        User entityManagerUser = entityManager.find(User.class, id);

        return new PersistenceContextResult(
                id,
                entityManager.contains(repositoryUser),
                entityManager.contains(entityManagerUser),
                repositoryUser == entityManagerUser,
                "The repository and EntityManager participate in the same transaction-scoped Persistence Context."
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
