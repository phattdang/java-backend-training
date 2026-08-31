package com.example.P01_SpringSecurityArchitecture.controller;

import com.example.P01_SpringSecurityArchitecture.dto.request.UserCreationRequest;
import com.example.P01_SpringSecurityArchitecture.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping("/me")
    ResponseEntity<?> me(Authentication authentication, Principal principal){
        System.out.println(authentication);
        System.out.println(principal);
        return ResponseEntity.status(HttpStatus.OK).body(userService.me());
    }

    @PostMapping
    ResponseEntity<?> create(@RequestBody @Valid UserCreationRequest request){
        return ResponseEntity.status(HttpStatus.OK).body(userService.create(request));
    }
}
