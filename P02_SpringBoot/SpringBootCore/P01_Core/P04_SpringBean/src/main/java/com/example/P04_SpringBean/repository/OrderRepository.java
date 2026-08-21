package com.example.P04_SpringBean.repository;

import org.springframework.stereotype.Repository;

@Repository
public class OrderRepository {

    public String findSampleOrder() {
        return "ORDER-001";
    }
}
