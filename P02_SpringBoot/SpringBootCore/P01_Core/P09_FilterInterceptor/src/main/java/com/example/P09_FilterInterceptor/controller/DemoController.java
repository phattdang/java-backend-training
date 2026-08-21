package com.example.P09_FilterInterceptor.controller;

import com.example.P09_FilterInterceptor.dto.CreateDemoRequest;
import com.example.P09_FilterInterceptor.dto.DemoResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@RestController
public class DemoController {
    private static final Logger log = LoggerFactory.getLogger(DemoController.class);

    @GetMapping("/api/demo/{id:\\d+}")
    public DemoResponse getDemo(
            @PathVariable Long id,
            @RequestParam String name,
            @RequestHeader(value = "X-Request-Id", required = false) String requestId
    ) {
        log.info("CONTROLLER GET");
        return new DemoResponse(
                id,
                name,
                null,
                requestId,
                "GET demo response",
                LocalDateTime.now()
        );
    }

    @PostMapping("/api/demo")
    public DemoResponse createDemo(@RequestBody CreateDemoRequest request) {
        log.info("CONTROLLER POST");
        return new DemoResponse(
                1L,
                request.name(),
                request.age(),
                null,
                "POST demo response",
                LocalDateTime.now()
        );
    }

    @GetMapping("/api/demo/error")
    public DemoResponse throwError() {
        log.info("CONTROLLER ERROR");
        throw new RuntimeException("Demo exception from controller");
    }

    @GetMapping("/api/public/hello")
    public DemoResponse publicHello() {
        log.info("CONTROLLER PUBLIC HELLO");
        return new DemoResponse(
                null,
                "public",
                null,
                null,
                "This endpoint passes Filter but is excluded from Interceptor",
                LocalDateTime.now()
        );
    }
}
