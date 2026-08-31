package com.example.P01_SpringSecurityArchitecture.controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.Map;
import java.util.Objects;

@RestController
@RequestMapping("/api")
public class DemoController {
    @GetMapping("/public")
    public String publicEndpoint(){
        System.out.println("During do filter!");
        return "Public endpoint!!!";
    }

    @GetMapping("/user")
    public String userEndpoint(){
        return "User endpoint!!!";
    }

    @GetMapping("/admin")
    public String adminEndpoint(){
        return "Admin endpoint!!!";
    }

    @GetMapping("/authentication")
    public Map<String, Object> authentication(Authentication authentication){
        return Map.of("name", authentication.getName(),

              "authenticated", authentication.isAuthenticated(),
                "principal", Objects.requireNonNull(authentication.getPrincipal()).toString(),
                "authorities", authentication.getAuthorities().toString()
        );
    }

    @GetMapping("/principal")
    public String principal(Principal principal) {
        return principal.getName();
    }

    @GetMapping("/security-context")
    public Map<String, Object> securityContext() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) return null;
        return Map.of(
                "name", authentication.getName(),
                "authenticated", authentication.isAuthenticated(),
                "principal", Objects.requireNonNull(authentication.getPrincipal()).toString(),
                "authorities", authentication.getAuthorities().toString()
        );
    }
}
