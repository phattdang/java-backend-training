package com.example.P06_RestController.service;

import com.example.P06_RestController.domain.User;
import com.example.P06_RestController.domain.UserStatus;
import com.example.P06_RestController.dto.CreateUserRequest;
import com.example.P06_RestController.dto.PatchUserRequest;
import com.example.P06_RestController.dto.UpdateUserRequest;
import com.example.P06_RestController.dto.UserResponse;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.Instant;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class UserService {
    private final ConcurrentHashMap<Long, User> users = new ConcurrentHashMap<>();
    private final AtomicLong nextId = new AtomicLong(1);

    public UserService() {
        addUser("Nguyen Van An", "an@example.com", UserStatus.ACTIVE);
        addUser("Tran Thi Binh", "binh@example.com", UserStatus.INACTIVE);
        addUser("Le Minh Cuong", "cuong@example.com", UserStatus.ACTIVE);
    }

    public List<UserResponse> findAll(String status, int page, int size) {
        validatePaging(page, size);
        UserStatus parsedStatus = parseStatus(status);

        return users.values().stream()
                .filter(user -> parsedStatus == null || user.getStatus() == parsedStatus)
                .sorted(Comparator.comparing(User::getId))
                .skip((long) page * size)
                .limit(size)
                .map(this::toResponse)
                .toList();
    }

    public UserResponse findById(Long id) {
        return toResponse(getUserOrThrow(id));
    }

    public UserResponse create(CreateUserRequest request) {
        validateRequired(request, "request body");
        validateRequired(request.name(), "name");
        validateRequired(request.email(), "email");
        validateRequired(request.status(), "status");

        return toResponse(addUser(request.name(), request.email(), request.status()));
    }

    public UserResponse update(Long id, UpdateUserRequest request) {
        validateRequired(request, "request body");
        validateRequired(request.name(), "name");
        validateRequired(request.email(), "email");
        validateRequired(request.status(), "status");

        User user = getUserOrThrow(id);
        user.setName(request.name().trim());
        user.setEmail(request.email().trim());
        user.setStatus(request.status());
        user.setUpdatedAt(Instant.now());
        return toResponse(user);
    }

    public UserResponse patch(Long id, PatchUserRequest request) {
        validateRequired(request, "request body");

        User user = getUserOrThrow(id);
        boolean changed = false;

        if (hasText(request.name())) {
            user.setName(request.name().trim());
            changed = true;
        }
        if (hasText(request.email())) {
            user.setEmail(request.email().trim());
            changed = true;
        }
        if (request.status() != null) {
            user.setStatus(request.status());
            changed = true;
        }

        if (!changed) {
            throw badRequest("PATCH body must contain at least one field: name, email, or status");
        }

        user.setUpdatedAt(Instant.now());
        return toResponse(user);
    }

    public void delete(Long id) {
        User removed = users.remove(id);
        if (removed == null) {
            throw notFound(id);
        }
    }

    private User addUser(String name, String email, UserStatus status) {
        Long id = nextId.getAndIncrement();
        Instant now = Instant.now();
        User user = new User(id, name.trim(), email.trim(), status, now, now);
        users.put(id, user);
        return user;
    }

    private User getUserOrThrow(Long id) {
        User user = users.get(id);
        if (user == null) {
            throw notFound(id);
        }
        return user;
    }

    private UserStatus parseStatus(String status) {
        if (!hasText(status)) {
            return null;
        }

        try {
            return UserStatus.valueOf(status.trim().toUpperCase(Locale.ROOT));
        } catch (IllegalArgumentException exception) {
            throw badRequest("status must be one of: ACTIVE, INACTIVE, BLOCKED");
        }
    }

    private void validatePaging(int page, int size) {
        if (page < 0) {
            throw badRequest("page must be greater than or equal to 0");
        }
        if (size < 1 || size > 100) {
            throw badRequest("size must be between 1 and 100");
        }
    }

    private void validateRequired(Object value, String fieldName) {
        if (value == null) {
            throw badRequest(fieldName + " is required");
        }
        if (value instanceof String text && !hasText(text)) {
            throw badRequest(fieldName + " must not be blank");
        }
    }

    private ResponseStatusException notFound(Long id) {
        return new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found with id: " + id);
    }

    private ResponseStatusException badRequest(String message) {
        return new ResponseStatusException(HttpStatus.BAD_REQUEST, message);
    }

    private boolean hasText(String value) {
        return value != null && !value.trim().isEmpty();
    }

    private UserResponse toResponse(User user) {
        return new UserResponse(
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getStatus(),
                user.getCreatedAt(),
                user.getUpdatedAt()
        );
    }
}
