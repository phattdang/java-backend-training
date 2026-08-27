package com.example.P04_EntityRelationships.userprofile.dto;

public record UserProfileResponse(
        Long userId,
        String userName,
        Long profileId,
        String bio
) {
}
