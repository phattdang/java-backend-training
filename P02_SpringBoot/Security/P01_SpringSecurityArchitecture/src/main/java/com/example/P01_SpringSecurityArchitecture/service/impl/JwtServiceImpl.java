package com.example.P01_SpringSecurityArchitecture.service.impl;

import com.example.P01_SpringSecurityArchitecture.config.JwtConfig;
import com.example.P01_SpringSecurityArchitecture.config.JwtProperties;
import com.example.P01_SpringSecurityArchitecture.service.JwtService;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.List;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class JwtServiceImpl implements JwtService {
    JwtProperties jwtConfig;

    @Override
    public String generateToken(Authentication authentication) {
        Date now = new Date();
        List<String> authorities = authentication.getAuthorities()
                        .stream()
                        .map(GrantedAuthority::getAuthority)
                        .toList();

        return Jwts.builder()
                .header()
                .add("typ", "JWT")
                .and()
                .subject(authentication.getName())
                .claim("authorities", authorities)
                .issuedAt(now)
                .expiration(new Date(now.getTime() + jwtConfig.getExpirationTime()))
                .signWith(getSigningKey(), Jwts.SIG.HS256)
                .compact();
    }

    @Override
    public boolean isValid(String token) {
        return false;
    }

    @Override
    public String extractUsername(String token) {
        return "";
    }

    private SecretKey getSigningKey() {
        return Keys.hmacShaKeyFor(jwtConfig.getSecretKey().getBytes());
    }
}
