package com.example.P08_NPlusOnePractice.service;

import com.example.P08_NPlusOnePractice.dto.BatchFetchingExplanation;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BatchFetchingService {

    public BatchFetchingExplanation explainBatchFetching() {
        return new BatchFetchingExplanation(
                "Hibernate batch fetching",
                List.of(
                        "SELECT employees WHERE department_id = 1",
                        "SELECT employees WHERE department_id = 2",
                        "SELECT employees WHERE department_id = 3",
                        "SELECT employees WHERE department_id = 4"
                ),
                List.of(
                        "SELECT employees WHERE department_id IN (?, ?, ?, ?)"
                ),
                "Batch fetching keeps relationships lazy, but Hibernate can initialize several lazy relationships in groups instead of one parent ID at a time.",
                "Documentation-only in this lab so the baseline N+1 endpoints remain visibly broken."
        );
    }
}
