package com.example.P01_ConfigurationProperties;

import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/config")
public class ConfigController {

    private final AppConfigValue appConfigValue;
    private final JwtProperties jwtProperties;

    public ConfigController(AppConfigValue appConfigValue, JwtProperties jwtProperties) {
        this.appConfigValue = appConfigValue;
        this.jwtProperties = jwtProperties;
    }

    @GetMapping
    public Map<String, Object> getConfig() {
        return Map.of(
                "message", appConfigValue.getMessage(),
                "jwtSecret", jwtProperties.getSecret(),
                "expiration", jwtProperties.getAccessTokenExpiration()
        );
    }
}
