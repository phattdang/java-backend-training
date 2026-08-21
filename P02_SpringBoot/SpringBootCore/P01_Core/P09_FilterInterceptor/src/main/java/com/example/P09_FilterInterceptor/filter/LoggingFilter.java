package com.example.P09_FilterInterceptor.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class LoggingFilter extends OncePerRequestFilter {
    private static final Logger log = LoggerFactory.getLogger(LoggingFilter.class);

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {
        long startTime = System.currentTimeMillis();

        log.info("FILTER BEFORE");
        log.info("FILTER request method={}, uri={}", request.getMethod(), request.getRequestURI());

        try {
            // Request di tiep vao phan con lai cua Servlet pipeline, gom DispatcherServlet va Spring MVC.
            filterChain.doFilter(request, response);
        } finally {
            long duration = System.currentTimeMillis() - startTime;
            log.info("FILTER AFTER");
            log.info("FILTER response status={}, durationMs={}", response.getStatus(), duration);
        }
    }
}
