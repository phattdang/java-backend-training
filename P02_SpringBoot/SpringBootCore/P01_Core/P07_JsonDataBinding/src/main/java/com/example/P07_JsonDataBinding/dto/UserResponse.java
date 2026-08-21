package com.example.P07_JsonDataBinding.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDate;
import java.time.LocalDateTime;

// Response DTO: Spring/Jackson serialize record nay thanh JSON.
public record UserResponse(
        Long id,

        @JsonProperty("full_name")
        String fullName,

        String email,
        LocalDate birthday,
        LocalDateTime createdAt,
        // @JsonIgnore demo: Java van co internalNote, nhung JSON response se khong co field nay.
        @JsonIgnore
        String internalNote
) { }
