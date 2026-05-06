package com.company.authservice.dto;

import com.company.authservice.utils.RoleEnum;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class RoleRequest {
    @NotNull(message = "User Guid is required.")
    @NotBlank(message = "User Guid cannot be empty.")
    private String userGuid;
    @NotNull(message = "Role is required.")
    @NotBlank(message = "Role cannot be empty.")
    private RoleEnum role;

    public RoleRequest() {
    }

    public String getUserGuid() {
        return userGuid;
    }

    public void setUserGuid(String userGuid) {
        this.userGuid = userGuid;
    }

    public RoleEnum getRole() {
        return role;
    }

    public void setRole(RoleEnum role) {
        this.role = role;
    }
}
