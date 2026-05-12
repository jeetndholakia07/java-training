package com.company.authservice.dto;

import com.company.authservice.utils.RoleEnum;
import jakarta.validation.constraints.NotNull;

public class RoleRequest {
    @NotNull(message = "Role is required.")
    private RoleEnum role;

    public RoleRequest() {
    }

    public RoleEnum getRole() {
        return role;
    }

    public void setRole(RoleEnum role) {
        this.role = role;
    }
}
