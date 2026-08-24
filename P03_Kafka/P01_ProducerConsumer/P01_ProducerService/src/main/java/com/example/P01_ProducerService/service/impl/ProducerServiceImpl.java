package com.example.P01_ProducerService.service.impl;

import com.example.P01_ProducerService.dto.OrderProducer;
import com.example.P01_ProducerService.service.ProducerService;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class ProducerServiceImpl implements ProducerService {
    private final KafkaTemplate<String, OrderProducer> kafkaTemplate;

    public ProducerServiceImpl(KafkaTemplate<String, OrderProducer> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    @Override
    public void send(String key, OrderProducer orderProducer) {
        kafkaTemplate.send("order-events", key, orderProducer)
                .whenComplete((result, ex) -> {
                    if (ex == null) {
                        var metadata = result.getRecordMetadata();

                        System.out.println(
                                "SUCCESS | topic=" + metadata.topic()
                                        + " | partition=" + metadata.partition()
                                        + " | offset=" + metadata.offset()
                        );
                    } else {
                        System.out.println(
                                "FAILED | " + ex.getMessage()
                        );
                    }
                });
    }
}
