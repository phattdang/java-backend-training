package com.example.P07_JsonDataBinding.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDate;

// Record DTO: Jackson map JSON object vao canonical constructor cua record.
public record CreateUserRequest(
        @JsonProperty("full_name")
        String fullName,

        String email,

        LocalDate birthday
) {
}
