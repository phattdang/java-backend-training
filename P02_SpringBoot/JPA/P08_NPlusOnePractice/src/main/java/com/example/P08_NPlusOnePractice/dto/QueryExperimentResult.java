package com.example.P08_NPlusOnePractice.dto;

public record QueryExperimentResult(
        String experiment,
        long queryCount,
        int rootEntityCount,
        String explanation
) {
}
