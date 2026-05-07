package com.company.authservice.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class UserLoginRequest {
    @NotNull(message = "Email is required.")
    @NotBlank(message = "Email cannot be blank.")
    private String email;
    @NotNull(message = "Password is required.")
    @NotBlank(message = "Password cannot be blank.")
    private String password;

    public UserLoginRequest(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public UserLoginRequest() {
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
