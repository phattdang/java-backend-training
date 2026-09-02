package com.example.P01_SpringSecurityArchitecture.service.impl;

import com.example.P01_SpringSecurityArchitecture.dto.request.LoginRequest;
import com.example.P01_SpringSecurityArchitecture.dto.response.AuthenticationResponse;
import com.example.P01_SpringSecurityArchitecture.entity.User;
import com.example.P01_SpringSecurityArchitecture.repository.UserRepository;
import com.example.P01_SpringSecurityArchitecture.service.AuthenticationService;
import com.example.P01_SpringSecurityArchitecture.service.JwtService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {
    UserRepository userRepository;
    AuthenticationManager authenticationManager;
    JwtService jwtService;

    @Override
    public AuthenticationResponse login(LoginRequest request) {
        Authentication authentication = authenticationManager.authenticate(
                UsernamePasswordAuthenticationToken.unauthenticated(request.getEmail(), request.getPassword())
        );

        return AuthenticationResponse.builder()
                .authenticated(authentication.isAuthenticated())
                .token(jwtService.generateToken(authentication))
                .build();
    }
}
