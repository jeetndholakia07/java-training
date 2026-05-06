package com.company.authservice.dto;

import com.company.authservice.utils.RoleEnum;

public class UserPayload {
    private String userGuid;
    private String username;
    private RoleEnum role;

    public UserPayload() {
    }

    public String getUserGuid() {
        return userGuid;
    }

    public void setUserGuid(String userGuid) {
        this.userGuid = userGuid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public RoleEnum getRole() {
        return role;
    }

    public void setRole(RoleEnum role) {
        this.role = role;
    }
}
