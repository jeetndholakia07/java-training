package com.company.authservice.dto;

import com.company.authservice.utils.RoleEnum;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record UserRegisterRequest(
        @NotNull(message = "Username is required.")
        @NotBlank(message = "Username cannot be blank.")
        String username,
        @NotNull(message = "Email is required.")
        @NotBlank(message = "Email cannot be blank.")
        String email,
        @NotNull(message = "Password is required.")
        @NotBlank(message = "Password cannot be blank.")
        String password,
        @NotNull(message = "Role is required.")
        RoleEnum role
) {
}
