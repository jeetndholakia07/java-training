package com.company.authservice.dto;

import com.company.authservice.utils.RoleEnum;

public record UserPayload(
        String userGuid,
        String username,
        RoleEnum role
) {
}
