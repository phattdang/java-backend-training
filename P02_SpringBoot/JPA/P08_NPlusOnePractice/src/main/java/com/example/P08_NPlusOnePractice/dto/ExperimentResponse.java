package com.example.P08_NPlusOnePractice.dto;

import java.util.List;

public record ExperimentResponse<T>(
        QueryExperimentResult result,
        List<T> data
) {
}
