package com.example.P05_RestAPI.service;

import com.example.P05_RestAPI.domain.User;
import com.example.P05_RestAPI.domain.UserStatus;
import com.example.P05_RestAPI.dto.CreateUserRequest;
import com.example.P05_RestAPI.dto.PatchUserRequest;
import com.example.P05_RestAPI.dto.UpdateUserRequest;
import com.example.P05_RestAPI.dto.UserResponse;
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
        createSampleUser("Nguyen Van An", "an@example.com", UserStatus.ACTIVE);
        createSampleUser("Tran Thi Binh", "binh@example.com", UserStatus.INACTIVE);
        createSampleUser("Le Minh Cuong", "cuong@example.com", UserStatus.ACTIVE);
    }

    public List<UserResponse> findAll(String status, int page, int size) {
        validatePageRequest(page, size);
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

        return toResponse(createSampleUser(request.name(), request.email(), request.status()));
    }

    public UserResponse update(Long id, UpdateUserRequest request) {
        validateRequired(request, "request body");
        validateRequired(request.name(), "name");
        validateRequired(request.email(), "email");
        validateRequired(request.status(), "status");

        User existingUser = getUserOrThrow(id);
        existingUser.setName(request.name().trim());
        existingUser.setEmail(request.email().trim());
        existingUser.setStatus(request.status());
        existingUser.setUpdatedAt(Instant.now());
        return toResponse(existingUser);
    }

    public UserResponse patch(Long id, PatchUserRequest request) {
        validateRequired(request, "request body");
        User existingUser = getUserOrThrow(id);
        boolean changed = false;

        if (hasText(request.name())) {
            existingUser.setName(request.name().trim());
            changed = true;
        }
        if (hasText(request.email())) {
            existingUser.setEmail(request.email().trim());
            changed = true;
        }
        if (request.status() != null) {
            existingUser.setStatus(request.status());
            changed = true;
        }

        if (!changed) {
            throw badRequest("PATCH body must contain at least one field: name, email, or status");
        }

        existingUser.setUpdatedAt(Instant.now());
        return toResponse(existingUser);
    }

    public void delete(Long id) {
        User removedUser = users.remove(id);
        if (removedUser == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found with id: " + id);
        }
    }

    private User createSampleUser(String name, String email, UserStatus status) {
        Long id = nextId.getAndIncrement();
        Instant now = Instant.now();
        User user = new User(id, name.trim(), email.trim(), status, now, now);
        users.put(id, user);
        return user;
    }

    private User getUserOrThrow(Long id) {
        User user = users.get(id);
        if (user == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found with id: " + id);
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

    private void validatePageRequest(int page, int size) {
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
