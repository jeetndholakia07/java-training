package com.company.authservice.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record UserLoginRequest(
        @NotNull(message = "Email is required.")
        @NotBlank(message = "Email cannot be blank.")
        String email,
        @NotNull(message = "Password is required.")
        @NotBlank(message = "Password cannot be blank.")
        String password
) {
}
