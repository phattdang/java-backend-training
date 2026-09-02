package com.example.P01_SpringSecurityArchitecture.service;

import com.example.P01_SpringSecurityArchitecture.dto.request.LoginRequest;
import com.example.P01_SpringSecurityArchitecture.dto.response.AuthenticationResponse;

public interface AuthenticationService {
    AuthenticationResponse login(LoginRequest request);
}
