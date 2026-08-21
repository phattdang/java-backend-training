package com.example.P07_JsonDataBinding.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDate;

// DTO dung de test Jackson mapping error: id phai la number, birthday phai dung ISO date yyyy-MM-dd.
public record MappingErrorRequest(
        Integer id,

        @JsonProperty("full_name")
        String fullName,

        LocalDate birthday
) {
}
