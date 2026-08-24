package com.example.P01_ProducerService.service;

import com.example.P01_ProducerService.dto.OrderProducer;

public interface ProducerService {
    void send(String key, OrderProducer orderProducer);
}
