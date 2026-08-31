package com.example.P01_SpringSecurityArchitecture.dto.request;

import com.example.P01_SpringSecurityArchitecture.entity.enums.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@NoArgsConstructor
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserCreationRequest {

    @Email
    @NotBlank
    String email;

    @NotBlank
    String password;

    @NotNull
    Role role;
}
