package com.example.P08_ExceptionHandling.controller;

import com.example.P08_ExceptionHandling.dto.CreateUserRequest;
import com.example.P08_ExceptionHandling.dto.UserResponse;
import com.example.P08_ExceptionHandling.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    // GET /users/{id}: neu id khong ton tai, service throw UserNotFoundException -> GlobalExceptionHandler -> 404.
    @GetMapping("/users/{id}")
    public UserResponse getUserById(@PathVariable Long id) {
        return userService.findById(id);
    }

    // POST /users: @Valid kich hoat Bean Validation cho @NotBlank, @Email, @Min truoc khi vao service.
    @PostMapping("/users")
    @ResponseStatus(HttpStatus.CREATED)
    public UserResponse createUser(@Valid @RequestBody CreateUserRequest request) {
        return userService.create(request);
    }

    // GET /users/unexpected-error: co tinh tao RuntimeException de demo handler 500.
    @GetMapping("/users/unexpected-error")
    public UserResponse unexpectedError() {
        userService.throwUnexpectedError();
        return null;
    }
}
