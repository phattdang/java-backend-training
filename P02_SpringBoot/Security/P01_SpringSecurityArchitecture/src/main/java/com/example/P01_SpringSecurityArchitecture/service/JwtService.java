package com.example.P01_SpringSecurityArchitecture.service;

import org.springframework.security.core.Authentication;

public interface JwtService {

    String generateToken(Authentication authentication);

    boolean isValid(String token);

    String extractUsername(String token);
}
