package com.example.P08_NPlusOnePractice.dto;

import java.util.List;

public record BatchFetchingExplanation(
        String concept,
        List<String> withoutBatching,
        List<String> withBatching,
        String explanation,
        String labStatus
) {
}
