package com.example.P01_ProducerService.controller;

import com.example.P01_ProducerService.dto.OrderProducer;
import com.example.P01_ProducerService.service.ProducerService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/messages")
public class MessageController {
    private final ProducerService producerService;

    public MessageController(ProducerService producerService) {
        this.producerService = producerService;
    }

    @PostMapping
    public String send(
            @RequestParam String key,
            @RequestBody OrderProducer orderProducer
    ) {
        producerService.send(key, orderProducer);
        return "Message sent";
    }
}
