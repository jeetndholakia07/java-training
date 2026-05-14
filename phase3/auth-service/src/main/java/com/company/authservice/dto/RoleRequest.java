package com.company.authservice.dto;

import com.company.authservice.utils.RoleEnum;
import jakarta.validation.constraints.NotNull;

public record RoleRequest(
        @NotNull(message = "Role is required.")
        RoleEnum role
) {
}
