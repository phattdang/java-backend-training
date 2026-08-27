package com.example.P04_EntityRelationships.userprofile.service;

import com.example.P04_EntityRelationships.userprofile.dto.CreateProfileRequest;
import com.example.P04_EntityRelationships.userprofile.dto.CreateUserRequest;
import com.example.P04_EntityRelationships.userprofile.dto.UserProfileResponse;
import com.example.P04_EntityRelationships.userprofile.entity.Profile;
import com.example.P04_EntityRelationships.userprofile.entity.User;
import com.example.P04_EntityRelationships.userprofile.repository.ProfileRepository;
import com.example.P04_EntityRelationships.userprofile.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
public class UserProfileService {

    private final UserRepository userRepository;
    private final ProfileRepository profileRepository;

    public UserProfileService(UserRepository userRepository, ProfileRepository profileRepository) {
        this.userRepository = userRepository;
        this.profileRepository = profileRepository;
    }

    public UserProfileResponse createUser(CreateUserRequest request) {
        User user = userRepository.save(new User(request.name()));
        return new UserProfileResponse(user.getId(), user.getName(), null, null);
    }

    @Transactional
    public UserProfileResponse createProfile(Long userId, CreateProfileRequest request) {
        User user = findUser(userId);
        if (user.getProfile() != null) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "User already has a profile: " + userId);
        }

        Profile profile = new Profile(request.bio());
        user.attachProfile(profile);
        // No cascade is configured: persist the Profile owning side explicitly.
        Profile savedProfile = profileRepository.save(profile);
        return toResponse(user, savedProfile);
    }

    @Transactional(readOnly = true)
    public UserProfileResponse findUserWithProfile(Long userId) {
        User user = findUser(userId);
        return toResponse(user, user.getProfile());
    }

    private User findUser(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found: " + id));
    }

    private UserProfileResponse toResponse(User user, Profile profile) {
        return new UserProfileResponse(
                user.getId(),
                user.getName(),
                profile == null ? null : profile.getId(),
                profile == null ? null : profile.getBio()
        );
    }
}
