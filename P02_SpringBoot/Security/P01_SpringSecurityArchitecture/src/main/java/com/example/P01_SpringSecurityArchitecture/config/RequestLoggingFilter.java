package com.example.P01_SpringSecurityArchitecture.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.jspecify.annotations.NonNull;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

public class RequestLoggingFilter extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {

        Authentication beforeAuth = SecurityContextHolder.getContext().getAuthentication();

        System.out.println("=== BEFORE NEXT FILTER ===");
        System.out.println("URI: " + request.getRequestURI());
        System.out.println("Authentication: " + beforeAuth);

        filterChain.doFilter(request, response);

        Authentication afterAuth = SecurityContextHolder.getContext().getAuthentication();

        System.out.println("=== AFTER NEXT FILTER ===");
        System.out.println("URI: " + request.getRequestURI());
        System.out.println("Authentication: " + afterAuth);
    }
}
