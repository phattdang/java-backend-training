package com.example.P01_SpringSecurityArchitecture.controller;

import com.example.P01_SpringSecurityArchitecture.dto.request.LoginRequest;
import com.example.P01_SpringSecurityArchitecture.dto.response.AuthenticationResponse;
import com.example.P01_SpringSecurityArchitecture.service.AuthenticationService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth/")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthController {
    AuthenticationService authenticationService;

    @PostMapping("/login")
    ResponseEntity<?> login(@RequestBody @Valid LoginRequest request){
        AuthenticationResponse response = authenticationService.login(request);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
