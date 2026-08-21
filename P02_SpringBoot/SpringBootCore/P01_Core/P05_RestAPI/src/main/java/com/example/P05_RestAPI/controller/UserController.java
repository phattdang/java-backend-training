package com.example.P05_RestAPI.controller;

import com.example.P05_RestAPI.dto.CreateUserRequest;
import com.example.P05_RestAPI.dto.PatchUserRequest;
import com.example.P05_RestAPI.dto.UpdateUserRequest;
import com.example.P05_RestAPI.dto.UserResponse;
import com.example.P05_RestAPI.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(
        value = "/api/users",
        produces = MediaType.APPLICATION_JSON_VALUE
)
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    // HTTP method: GET. Query parameters: status, page, size. Safe and idempotent read.
    @GetMapping
    public ResponseEntity<List<UserResponse>> getUsers(
            @RequestParam(required = false) String status,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        return ResponseEntity.ok(userService.findAll(status, page, size));
    }

    // HTTP method: GET. Path parameter: id. Safe and idempotent read.
    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> getUserById(@PathVariable Long id) {
        return ResponseEntity.ok(userService.findById(id));
    }

    // HTTP method: POST. Request body: JSON. Not idempotent: each valid request creates a new user id.
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UserResponse> createUser(@RequestBody CreateUserRequest request) {
        UserResponse response = userService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // HTTP method: PUT. Path parameter: id. Request body: full JSON replacement. Idempotent update.
    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UserResponse> updateUser(
            @PathVariable Long id,
            @RequestBody UpdateUserRequest request
    ) {
        return ResponseEntity.ok(userService.update(id, request));
    }

    // HTTP method: PATCH. Path parameter: id. Request body: partial JSON update. Not necessarily idempotent in general.
    @PatchMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UserResponse> patchUser(
            @PathVariable Long id,
            @RequestBody PatchUserRequest request
    ) {
        return ResponseEntity.ok(userService.patch(id, request));
    }

    // HTTP method: DELETE. Path parameter: id. Idempotent intent; this demo returns 404 if already deleted.
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
