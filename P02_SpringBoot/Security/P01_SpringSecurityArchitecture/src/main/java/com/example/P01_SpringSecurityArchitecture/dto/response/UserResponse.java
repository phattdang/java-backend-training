package com.example.P01_SpringSecurityArchitecture.dto.response;

import com.example.P01_SpringSecurityArchitecture.entity.enums.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.FieldDefaults;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class UserResponse {
    Long id;
    String email;
    Role role;
}
