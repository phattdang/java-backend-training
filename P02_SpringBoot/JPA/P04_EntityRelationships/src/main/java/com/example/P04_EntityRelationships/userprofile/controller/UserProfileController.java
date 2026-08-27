package com.example.P04_EntityRelationships.userprofile.controller;

import com.example.P04_EntityRelationships.userprofile.dto.CreateProfileRequest;
import com.example.P04_EntityRelationships.userprofile.dto.CreateUserRequest;
import com.example.P04_EntityRelationships.userprofile.dto.UserProfileResponse;
import com.example.P04_EntityRelationships.userprofile.service.UserProfileService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/practice")
public class UserProfileController {

    private final UserProfileService service;

    public UserProfileController(UserProfileService service) {
        this.service = service;
    }

    // Part 7.1 + 7.5
    // Creates the User used by the User 1-1 Profile relationship scenario.
    @PostMapping("/users")
    @ResponseStatus(HttpStatus.CREATED)
    public UserProfileResponse createUser(@Valid @RequestBody CreateUserRequest request) {
        return service.createUser(request);
    }

    // Part 7.2-7.5 + 7.9-7.11 + 7.13-7.15
    // Profile owns profiles.user_id; service.createProfile() synchronizes User.profile and Profile.user.
    @PostMapping("/users/{userId}/profile")
    @ResponseStatus(HttpStatus.CREATED)
    public UserProfileResponse createProfile(
            @PathVariable Long userId,
            @Valid @RequestBody CreateProfileRequest request) {
        return service.createProfile(userId, request);
    }

    // Part 7.4 + 7.5 + 7.10 + 7.11
    // Reads the inverse User.profile field and returns a DTO instead of a recursive entity graph.
    @GetMapping("/users/{userId}")
    public UserProfileResponse findUserWithProfile(@PathVariable Long userId) {
        return service.findUserWithProfile(userId);
    }
}
