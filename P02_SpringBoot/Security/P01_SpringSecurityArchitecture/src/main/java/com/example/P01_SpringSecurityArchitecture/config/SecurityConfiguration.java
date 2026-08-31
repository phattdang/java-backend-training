package com.example.P01_SpringSecurityArchitecture.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {

    static final String[] PUBLIC_ENDPOINT = new String[] {
            "/api/public",
            "/api/authentication",
            "/api/principal",
            "/api/security-context",
            "/api/users",
    };
    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity, RequestLoggingFilter requestLoggingFilter) throws Exception {
        httpSecurity
                .csrf(AbstractHttpConfigurer::disable)
                .cors(Customizer.withDefaults())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(PUBLIC_ENDPOINT).permitAll()
                        .requestMatchers("/api/admin").hasRole("ADMIN")
                        .anyRequest().authenticated()
                )
                .formLogin(Customizer.withDefaults());
//                .formLogin(AbstractHttpConfigurer::disable);
//                .formLogin(form -> form.defaultSuccessUrl("/api/authentication", true));

        httpSecurity.addFilterBefore(
                requestLoggingFilter,
                UsernamePasswordAuthenticationFilter.class
        );

        return httpSecurity.build();
    }

    @Bean
    RequestLoggingFilter requestLoggingFilter(){
        return new RequestLoggingFilter();
    }

}
