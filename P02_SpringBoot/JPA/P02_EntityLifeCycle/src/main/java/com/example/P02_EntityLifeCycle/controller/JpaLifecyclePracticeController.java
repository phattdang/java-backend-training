package com.example.P02_EntityLifeCycle.controller;

import com.example.P02_EntityLifeCycle.dto.CreateUserRequest;
import com.example.P02_EntityLifeCycle.dto.DirtyCheckingResult;
import com.example.P02_EntityLifeCycle.dto.EntityManagerOperationResult;
import com.example.P02_EntityLifeCycle.dto.FindResult;
import com.example.P02_EntityLifeCycle.dto.FlushResult;
import com.example.P02_EntityLifeCycle.dto.LifecycleStateResult;
import com.example.P02_EntityLifeCycle.dto.PersistenceContextResult;
import com.example.P02_EntityLifeCycle.dto.ReferenceResult;
import com.example.P02_EntityLifeCycle.dto.UpdateUserNameRequest;
import com.example.P02_EntityLifeCycle.entity.User;
import com.example.P02_EntityLifeCycle.service.DirtyCheckingService;
import com.example.P02_EntityLifeCycle.service.EntityManagerOperationService;
import com.example.P02_EntityLifeCycle.service.FlushService;
import com.example.P02_EntityLifeCycle.service.LifecycleStateService;
import com.example.P02_EntityLifeCycle.service.PersistenceContextService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/practice")
public class JpaLifecyclePracticeController {

    private final LifecycleStateService lifecycleStateService;
    private final PersistenceContextService persistenceContextService;
    private final DirtyCheckingService dirtyCheckingService;
    private final FlushService flushService;
    private final EntityManagerOperationService entityManagerOperationService;

    public JpaLifecyclePracticeController(
            LifecycleStateService lifecycleStateService,
            PersistenceContextService persistenceContextService,
            DirtyCheckingService dirtyCheckingService,
            FlushService flushService,
            EntityManagerOperationService entityManagerOperationService) {
        this.lifecycleStateService = lifecycleStateService;
        this.persistenceContextService = persistenceContextService;
        this.dirtyCheckingService = dirtyCheckingService;
        this.flushService = flushService;
        this.entityManagerOperationService = entityManagerOperationService;
    }

    @PostMapping("/users")
    @ResponseStatus(HttpStatus.CREATED)
    public User createUser(@Valid @RequestBody CreateUserRequest request) {
        return entityManagerOperationService.createUsingRepository(request);
    }

    @GetMapping("/users/{id}")
    public User findUser(@PathVariable Long id) {
        return entityManagerOperationService.findUsingRepository(id);
    }

    @PostMapping("/lifecycle/transient")
    public LifecycleStateResult transientState() {
        return lifecycleStateService.demonstrateTransientState();
    }

    @PostMapping("/lifecycle/managed/{id}")
    public LifecycleStateResult managedState(@PathVariable Long id) {
        return lifecycleStateService.demonstrateManagedState(id);
    }

    @PostMapping("/lifecycle/detached/{id}")
    public LifecycleStateResult detachedState(
            @PathVariable Long id,
            @Valid @RequestBody UpdateUserNameRequest request) {
        return lifecycleStateService.demonstrateDetachedState(id, request.name());
    }

    @PostMapping("/lifecycle/removed/{id}")
    public LifecycleStateResult removedState(@PathVariable Long id) {
        return lifecycleStateService.demonstrateRemovedState(id);
    }

    @GetMapping("/context/managed/{id}")
    public LifecycleStateResult managedByPersistenceContext(@PathVariable Long id) {
        return persistenceContextService.demonstrateManagedByPersistenceContext(id);
    }

    @GetMapping("/context/first-level-cache/{id}")
    public PersistenceContextResult firstLevelCache(@PathVariable Long id) {
        return persistenceContextService.demonstrateFirstLevelCache(id);
    }

    @GetMapping("/context/identity/{id}")
    public PersistenceContextResult entityIdentity(@PathVariable Long id) {
        return persistenceContextService.demonstrateEntityIdentity(id);
    }

    @PostMapping("/context/clear/{id}")
    public LifecycleStateResult clearPersistenceContext(@PathVariable Long id) {
        return persistenceContextService.demonstrateClearPersistenceContext(id);
    }

    @PutMapping("/dirty-checking/{id}")
    public DirtyCheckingResult dirtyChecking(
            @PathVariable Long id,
            @Valid @RequestBody UpdateUserNameRequest request) {
        return dirtyCheckingService.demonstrateDirtyChecking(id, request.name());
    }

    @PostMapping("/flush/{id}")
    public FlushResult flush(
            @PathVariable Long id,
            @Valid @RequestBody UpdateUserNameRequest request) {
        return flushService.demonstrateFlush(id, request.name());
    }

    @PostMapping("/entity-manager/persist")
    @ResponseStatus(HttpStatus.CREATED)
    public EntityManagerOperationResult persist(@Valid @RequestBody CreateUserRequest request) {
        return entityManagerOperationService.demonstratePersist(request);
    }

    @PostMapping("/entity-manager/merge/{id}")
    public EntityManagerOperationResult merge(
            @PathVariable Long id,
            @Valid @RequestBody UpdateUserNameRequest request) {
        return entityManagerOperationService.demonstrateMerge(id, request.name());
    }

    @PostMapping("/entity-manager/remove/{id}")
    public EntityManagerOperationResult remove(@PathVariable Long id) {
        return entityManagerOperationService.demonstrateRemove(id);
    }

    @GetMapping("/entity-manager/find/{id}")
    public FindResult find(@PathVariable Long id) {
        return entityManagerOperationService.demonstrateFind(id);
    }

    @GetMapping("/entity-manager/reference/{id}")
    public ReferenceResult reference(@PathVariable Long id) {
        return entityManagerOperationService.demonstrateGetReference(id);
    }

    @GetMapping("/entity-manager/compare/{id}")
    public PersistenceContextResult compareRepositoryAndEntityManager(@PathVariable Long id) {
        return entityManagerOperationService.compareRepositoryAndEntityManager(id);
    }
}
