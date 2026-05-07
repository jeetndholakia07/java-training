package com.company.authservice.dto;

import com.company.authservice.utils.RoleEnum;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class UserRegisterRequest {
    @NotNull(message = "Username is required.")
    @NotBlank(message = "Username cannot be blank.")
    private String username;
    @NotNull(message = "Email is required.")
    @NotBlank(message = "Email cannot be blank.")
    private String email;
    @NotNull(message = "Password is required.")
    @NotBlank(message = "Password cannot be blank.")
    private String password;
    @NotNull(message = "Role is required.")
    private RoleEnum role;

    public UserRegisterRequest() {
    }

    public UserRegisterRequest(String username, String email, String password, RoleEnum role) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.role = role;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
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

    public RoleEnum getRole() {
        return role;
    }

    public void setRole(RoleEnum role) {
        this.role = role;
    }
}
