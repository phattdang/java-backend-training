package com.example.P08_NPlusOnePractice.service;

import jakarta.persistence.EntityManagerFactory;
import org.hibernate.SessionFactory;
import org.hibernate.stat.Statistics;
import org.springframework.stereotype.Component;

@Component
public class QueryCounter {

    private final Statistics statistics;

    public QueryCounter(EntityManagerFactory entityManagerFactory) {
        this.statistics = entityManagerFactory.unwrap(SessionFactory.class).getStatistics();
    }

    public void reset() {
        statistics.clear();
    }

    public long preparedStatementCount() {
        return statistics.getPrepareStatementCount();
    }
}
