package com.example.P01_ConfigurationProperties;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class AppConfigValue {

    @Value("${app.message}")
    private String message;

    public String getMessage() {
        return message;
    }
}
