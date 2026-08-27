package com.example.P03_JpaRepository.service;

import com.example.P03_JpaRepository.dto.CreateUserRequest;
import com.example.P03_JpaRepository.dto.RepositoryOperationResult;
import com.example.P03_JpaRepository.dto.UpdateUserRequest;
import com.example.P03_JpaRepository.entity.User;
import com.example.P03_JpaRepository.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Service
public class RepositoryCrudService {

    private final UserRepository userRepository;

    public RepositoryCrudService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public RepositoryOperationResult saveUser(CreateUserRequest request) {
        User newUser = new User(request.name(), request.email());
        User savedUser = userRepository.save(newUser);

        return new RepositoryOperationResult(
                "save() - new entity",
                "save() detects a new entity and uses persist-like behavior.",
                savedUser
        );
    }

    public RepositoryOperationResult updateUserWithSave(Long id, UpdateUserRequest request) {
        // findById() finishes first; the returned entity is then passed back to save().
        // Because it is not new, Spring Data JPA uses merge-like behavior.
        User existingUser = findUserOrThrow(id);
        existingUser.setName(request.name());
        existingUser.setEmail(request.email());
        User savedUser = userRepository.save(existingUser);

        return new RepositoryOperationResult(
                "save() - existing entity",
                "save() detects a non-new entity and uses merge-like behavior when appropriate.",
                savedUser
        );
    }

    public RepositoryOperationResult saveAll(List<CreateUserRequest> requests) {
        List<User> users = requests.stream()
                .map(request -> new User(request.name(), request.email()))
                .toList();
        List<User> savedUsers = userRepository.saveAll(users);

        return new RepositoryOperationResult(
                "saveAll()",
                "Saves multiple entities through the repository API; this example does not configure JDBC batching.",
                savedUsers
        );
    }

    public RepositoryOperationResult findById(Long id) {
        Optional<User> optionalUser = userRepository.findById(id);
        User user = optionalUser.orElseThrow(() -> userNotFound(id));

        return new RepositoryOperationResult(
                "findById() + Optional<T>",
                "findById() returns Optional<User>; orElseThrow() handles the empty case.",
                user
        );
    }

    public RepositoryOperationResult findAll() {
        List<User> users = userRepository.findAll();
        return new RepositoryOperationResult(
                "findAll()",
                "Loads all users through JpaRepository without pagination.",
                users
        );
    }

    public RepositoryOperationResult existsById(Long id) {
        boolean exists = userRepository.existsById(id);
        return new RepositoryOperationResult(
                "existsById()",
                "Checks whether a row exists for the supplied primary key.",
                exists
        );
    }

    public RepositoryOperationResult count() {
        long count = userRepository.count();
        return new RepositoryOperationResult(
                "count()",
                "Counts all users in the table.",
                count
        );
    }

    public RepositoryOperationResult delete(User user) {
        userRepository.delete(user);
        return new RepositoryOperationResult(
                "delete(entity)",
                "Loads the entity first, then passes that entity to delete().",
                user.getId()
        );
    }

    public RepositoryOperationResult deleteById(Long id) {
        userRepository.deleteById(id);
        return new RepositoryOperationResult(
                "deleteById()",
                "Deletes by primary key. Inspect the SQL log to observe repository behavior.",
                id
        );
    }

    public RepositoryOperationResult deleteAll() {
        long countBeforeDelete = userRepository.count();
        userRepository.deleteAll();
        return new RepositoryOperationResult(
                "deleteAll()",
                "Learning-only operation: removes every User entity from the table.",
                countBeforeDelete
        );
    }

    public User findUserOrThrow(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> userNotFound(id));
    }

    private ResponseStatusException userNotFound(Long id) {
        return new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found: " + id);
    }
}
