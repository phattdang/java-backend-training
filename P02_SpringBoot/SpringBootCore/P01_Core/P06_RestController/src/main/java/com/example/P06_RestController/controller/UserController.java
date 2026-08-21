package com.example.P06_RestController.controller;

import com.example.P06_RestController.dto.CreateUserRequest;
import com.example.P06_RestController.dto.PatchUserRequest;
import com.example.P06_RestController.dto.UpdateUserRequest;
import com.example.P06_RestController.dto.UserResponse;
import com.example.P06_RestController.service.UserService;
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
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping(produces = MediaType.APPLICATION_JSON_VALUE)
public class UserController {
    private static final String REQUEST_ID_HEADER = "X-Request-Id";

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    // @RequestParam demo: status, page, size. Mapping nhieu URL: /api/users va /api/members.
    @GetMapping({"/api/users", "/api/members"})
    public ResponseEntity<List<UserResponse>> getUsers(
            @RequestParam(required = false) String status,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestHeader(value = REQUEST_ID_HEADER, required = false) String requestId
    ) {
        List<UserResponse> response = userService.findAll(status, page, size);
        return withRequestId(ResponseEntity.ok(), requestId).body(response);
    }

    // Direct object return demo: Spring tu serialize UserResponse thanh JSON response DTO.
    @GetMapping(value = "/api/users/{id}/direct", produces = MediaType.APPLICATION_JSON_VALUE)
    public UserResponse getUserDirect(@PathVariable Long id) {
        return userService.findById(id);
    }

    // @PathVariable + produces demo: id nam tren URI, endpoint tra application/json.
    @GetMapping(value = "/api/users/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UserResponse> getUserById(
            @PathVariable Long id,
            @RequestHeader(value = REQUEST_ID_HEADER, required = false) String requestId
    ) {
        return withRequestId(ResponseEntity.ok(), requestId).body(userService.findById(id));
    }

    // @RequestBody + consumes demo: POST chi nhan application/json. ResponseEntity demo: 201 + Location header.
    @PostMapping(value = "/api/users", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UserResponse> createUser(
            @RequestBody CreateUserRequest request,
            @RequestHeader(value = REQUEST_ID_HEADER, required = false) String requestId
    ) {
        UserResponse response = userService.create(request);
        URI location = URI.create("/api/users/" + response.id());

        return withRequestId(ResponseEntity.created(location), requestId)
                .body(response);
    }

    // @RequestBody demo: PUT nhan full update DTO. Idempotent khi gui cung mot body nhieu lan.
    @PutMapping(value = "/api/users/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UserResponse> updateUser(
            @PathVariable Long id,
            @RequestBody UpdateUserRequest request,
            @RequestHeader(value = REQUEST_ID_HEADER, required = false) String requestId
    ) {
        return withRequestId(ResponseEntity.ok(), requestId)
                .body(userService.update(id, request));
    }

    // @RequestBody demo: PATCH nhan partial update DTO, chi field nao gui len thi update field do.
    @PatchMapping(value = "/api/users/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UserResponse> patchUser(
            @PathVariable Long id,
            @RequestBody PatchUserRequest request,
            @RequestHeader(value = REQUEST_ID_HEADER, required = false) String requestId
    ) {
        return withRequestId(ResponseEntity.ok(), requestId)
                .body(userService.patch(id, request));
    }

    // Response status demo: DELETE thanh cong tra 204 No Content, khong co response body.
    @DeleteMapping("/api/users/{id}")
    public ResponseEntity<Void> deleteUser(
            @PathVariable Long id,
            @RequestHeader(value = REQUEST_ID_HEADER, required = false) String requestId
    ) {
        userService.delete(id);
        return withRequestId(ResponseEntity.status(HttpStatus.NO_CONTENT), requestId)
                .build();
    }

    // Response header demo: neu client gui X-Request-Id thi API tra lai header do trong response.
    private ResponseEntity.BodyBuilder withRequestId(ResponseEntity.BodyBuilder builder, String requestId) {
        if (requestId != null && !requestId.trim().isEmpty()) {
            return builder.header(REQUEST_ID_HEADER, requestId.trim());
        }
        return builder;
    }
}
