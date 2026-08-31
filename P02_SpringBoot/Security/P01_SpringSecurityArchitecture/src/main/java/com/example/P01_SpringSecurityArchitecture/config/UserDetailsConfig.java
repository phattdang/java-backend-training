package com.example.P01_SpringSecurityArchitecture.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

//@Configuration
public class UserDetailsConfig {
//    @Bean
    UserDetailsService userDetailsService(PasswordEncoder passwordEncoder){
        UserDetails user = User.builder()
                .username("user")
                .password(passwordEncoder.encode("12345"))
                .roles("USER")
                .build();
        UserDetails admin = User.builder()
                .username("admin")
                .password(passwordEncoder.encode("12345"))
                .roles("ADMIN")
                .build();

        return new InMemoryUserDetailsManager(user, admin);
    }
}
