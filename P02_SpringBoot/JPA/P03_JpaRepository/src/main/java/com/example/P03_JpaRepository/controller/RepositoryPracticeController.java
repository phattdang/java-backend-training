package com.example.P03_JpaRepository.controller;

import com.example.P03_JpaRepository.dto.CreateUserRequest;
import com.example.P03_JpaRepository.dto.RepositoryOperationResult;
import com.example.P03_JpaRepository.dto.UpdateUserRequest;
import com.example.P03_JpaRepository.service.RepositoryComparisonService;
import com.example.P03_JpaRepository.service.RepositoryCrudService;
import com.example.P03_JpaRepository.service.RepositoryFlushService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/practice")
public class RepositoryPracticeController {

    private final RepositoryCrudService crudService;
    private final RepositoryFlushService flushService;
    private final RepositoryComparisonService comparisonService;

    public RepositoryPracticeController(
            RepositoryCrudService crudService,
            RepositoryFlushService flushService,
            RepositoryComparisonService comparisonService) {
        this.crudService = crudService;
        this.flushService = flushService;
        this.comparisonService = comparisonService;
    }

    // Part 4.6 - save() for a new entity
    // Calls RepositoryCrudService.saveUser(); save() decides internally whether the entity is new.
    @PostMapping("/users")
    @ResponseStatus(HttpStatus.CREATED)
    public RepositoryOperationResult createUser(@Valid @RequestBody CreateUserRequest request) {
        return crudService.saveUser(request);
    }

    // Part 4.6 - save() for an existing entity
    // Calls RepositoryCrudService.updateUserWithSave(); demonstrates merge-like behavior when appropriate.
    @PutMapping("/users/{id}")
    public RepositoryOperationResult updateUser(
            @PathVariable Long id,
            @Valid @RequestBody UpdateUserRequest request) {
        return crudService.updateUserWithSave(id, request);
    }

    // Part 4.7 - saveAll()
    // Calls RepositoryCrudService.saveAll(); demonstrates repository batch API behavior, not JDBC batching.
    @PostMapping("/users/batch")
    @ResponseStatus(HttpStatus.CREATED)
    public RepositoryOperationResult createUsers(
            @RequestBody List<@Valid CreateUserRequest> requests) {
        return crudService.saveAll(requests);
    }

    // Part 4.8 + 4.17 - findById() + Optional<T>
    // Calls RepositoryCrudService.findById(); unwraps Optional<User> with orElseThrow().
    @GetMapping("/users/{id}")
    public RepositoryOperationResult findUser(@PathVariable Long id) {
        return crudService.findById(id);
    }

    // Part 4.9 - findAll()
    // Calls RepositoryCrudService.findAll(); uses the JpaRepository version without pagination.
    @GetMapping("/users")
    public RepositoryOperationResult findAllUsers() {
        return crudService.findAll();
    }

    // Part 4.10 - existsById()
    // Calls RepositoryCrudService.existsById(); returns a simple boolean result.
    @GetMapping("/users/{id}/exists")
    public RepositoryOperationResult userExists(@PathVariable Long id) {
        return crudService.existsById(id);
    }

    // Part 4.11 - count()
    // Calls RepositoryCrudService.count(); returns the total number of User entities.
    @GetMapping("/users/count")
    public RepositoryOperationResult countUsers() {
        return crudService.count();
    }

    // Part 4.12 - delete(entity)
    // Calls RepositoryCrudService.findUserOrThrow() first, then RepositoryCrudService.delete(user).
    @DeleteMapping("/users/entity/{id}")
    public RepositoryOperationResult deleteUserEntity(@PathVariable Long id) {
        return crudService.delete(crudService.findUserOrThrow(id));
    }

    // Part 4.13 - deleteById()
    // Calls RepositoryCrudService.deleteById(); inspect the SQL log to observe repository behavior.
    @DeleteMapping("/users/{id}")
    public RepositoryOperationResult deleteUserById(@PathVariable Long id) {
        return crudService.deleteById(id);
    }

    // Part 4.14 - deleteAll(); WARNING: removes every User row
    // Calls RepositoryCrudService.deleteAll(); this destructive endpoint is for learning only.
    @DeleteMapping("/users")
    public RepositoryOperationResult deleteAllUsers() {
        return crudService.deleteAll();
    }

    // Part 4.15 - flush()
    // Calls RepositoryFlushService.flushUpdate(); forces SQL synchronization before commit.
    @PostMapping("/users/{id}/flush")
    public RepositoryOperationResult flushUserUpdate(
            @PathVariable Long id,
            @Valid @RequestBody UpdateUserRequest request) {
        return flushService.flushUpdate(id, request);
    }

    // Part 4.16 - saveAndFlush()
    // Calls RepositoryFlushService.saveAndFlush(); saves and flushes but does not commit immediately.
    @PostMapping("/users/save-and-flush")
    @ResponseStatus(HttpStatus.CREATED)
    public RepositoryOperationResult saveAndFlush(@Valid @RequestBody CreateUserRequest request) {
        return flushService.saveAndFlush(request);
    }

    // Part 4.1-4.5 + 4.18 - repository abstractions, differences, and runtime implementation
    // Calls RepositoryComparisonService.compareRepositories(); no custom CRUD implementation is created.
    @GetMapping("/repositories")
    public RepositoryOperationResult compareRepositories() {
        return comparisonService.compareRepositories();
    }
}
