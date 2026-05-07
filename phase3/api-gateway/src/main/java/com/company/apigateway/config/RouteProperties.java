package com.company.apigateway.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

@ConfigurationProperties(prefix = "security")
public class RouteProperties {
    private List<String> publicRoutes;
    private List<RoleRoute> roleRoutes;

    public List<String> getPublicRoutes() {
        return publicRoutes;
    }

    public void setPublicRoutes(List<String> publicRoutes) {
        this.publicRoutes = publicRoutes;
    }

    public List<RoleRoute> getRoleRoutes() {
        return roleRoutes;
    }

    public void setRoleRoutes(List<RoleRoute> roleRoutes) {
        this.roleRoutes = roleRoutes;
    }
}
