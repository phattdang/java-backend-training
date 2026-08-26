package com.example.P01_EntityMappingBasic.controller;

import com.example.P01_EntityMappingBasic.dto.CreateSequenceUserRequest;
import com.example.P01_EntityMappingBasic.dto.CreateUserRequest;
import com.example.P01_EntityMappingBasic.entity.SequenceUser;
import com.example.P01_EntityMappingBasic.entity.User;
import com.example.P01_EntityMappingBasic.service.JpaPracticeService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/practice")
public class JpaPracticeController {

    private final JpaPracticeService service;

    public JpaPracticeController(JpaPracticeService service) {
        this.service = service;
    }

    @PostMapping("/users")
    @ResponseStatus(HttpStatus.CREATED)
    public User createUser(@Valid @RequestBody CreateUserRequest request) {
        return service.createUser(request);
    }

    @GetMapping("/users")
    public List<User> findAllUsers() {
        return service.findAllUsers();
    }

    @GetMapping("/users/{id}")
    public User findUser(@PathVariable long id) {
        return service.findUser(id);
    }

    @PostMapping("/sequence-users")
    @ResponseStatus(HttpStatus.CREATED)
    public SequenceUser createSequenceUser(@Valid @RequestBody CreateSequenceUserRequest request) {
        return service.createSequenceUser(request);
    }
}
