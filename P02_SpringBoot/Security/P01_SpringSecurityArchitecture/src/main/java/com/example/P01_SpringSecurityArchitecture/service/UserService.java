package com.example.P01_SpringSecurityArchitecture.service;

import com.example.P01_SpringSecurityArchitecture.dto.request.UserCreationRequest;
import com.example.P01_SpringSecurityArchitecture.dto.response.UserResponse;
import com.example.P01_SpringSecurityArchitecture.entity.User;

public interface UserService {
    User create(UserCreationRequest request);
    UserResponse me();
}
