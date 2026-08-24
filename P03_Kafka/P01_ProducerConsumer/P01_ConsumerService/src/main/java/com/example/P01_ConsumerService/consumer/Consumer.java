package com.example.P01_ConsumerService.consumer;

import com.example.P01_ConsumerService.dto.OrderConsumer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class Consumer {
    @KafkaListener(topics = "order-events")
    public void consume(ConsumerRecord<String, OrderConsumer> record) {
        System.out.println("----------------------");
        System.out.println("Topic: " + record.topic());
        System.out.println("Key: " + record.key());
        System.out.println("Value: " + record.value());
        System.out.println("Partition: " + record.partition());
        System.out.println("Offset: " + record.offset());
    }

//    @KafkaListener(topics = "order-events")
//    public void consume(OrderConsumer record) {
//        System.out.println(record);
//    }
}
