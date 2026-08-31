package com.example.P01_SpringSecurityArchitecture.service.impl;

import com.example.P01_SpringSecurityArchitecture.dto.request.UserCreationRequest;
import com.example.P01_SpringSecurityArchitecture.dto.response.UserResponse;
import com.example.P01_SpringSecurityArchitecture.entity.User;
import com.example.P01_SpringSecurityArchitecture.repository.UserRepository;
import com.example.P01_SpringSecurityArchitecture.service.UserService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    UserRepository userRepository;
    PasswordEncoder passwordEncoder;

    @Override
    public User create(UserCreationRequest request) {
        User user = new User();
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(request.getRole());
        user.setEnabled(true);
        user.setLocked(false);
        return userRepository.save(user);
    }

    @Override
    public UserResponse me() {
        return null;
    }
}
