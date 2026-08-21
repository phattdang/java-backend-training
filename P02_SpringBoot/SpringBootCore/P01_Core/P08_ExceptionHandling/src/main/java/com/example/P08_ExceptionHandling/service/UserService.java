package com.example.P08_ExceptionHandling.service;

import com.example.P08_ExceptionHandling.dto.CreateUserRequest;
import com.example.P08_ExceptionHandling.dto.UserResponse;
import com.example.P08_ExceptionHandling.exception.EmailAlreadyExistsException;
import com.example.P08_ExceptionHandling.exception.UserNotFoundException;
import com.example.P08_ExceptionHandling.model.User;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class UserService {
    private final Map<Long, User> users = new ConcurrentHashMap<>();
    private final AtomicLong nextId = new AtomicLong(1);

    public UserService() {
        addUser("Existing User", "existing@example.com", 25);
    }

    public UserResponse findById(Long id) {
        User user = users.get(id);
        if (user == null) {
            throw new UserNotFoundException(id);
        }
        return toResponse(user);
    }

    public UserResponse create(CreateUserRequest request) {
        boolean emailExists = users.values().stream()
                .anyMatch(user -> user.getEmail().equalsIgnoreCase(request.email()));

        if (emailExists) {
            throw new EmailAlreadyExistsException(request.email());
        }

        return toResponse(addUser(request.fullName(), request.email(), request.age()));
    }

    public void throwUnexpectedError() {
        throw new IllegalStateException("Unexpected demo error from service layer");
    }

    private User addUser(String fullName, String email, int age) {
        Long id = nextId.getAndIncrement();
        User user = new User(id, fullName, email, age);
        users.put(id, user);
        return user;
    }

    private UserResponse toResponse(User user) {
        return new UserResponse(
                user.getId(),
                user.getFullName(),
                user.getEmail(),
                user.getAge()
        );
    }
}
