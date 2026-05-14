package com.company.authservice.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.jspecify.annotations.NonNull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

public class GatewayFilter extends OncePerRequestFilter {
    @Value("${GATEWAY_SECRET}")
    private String gatewaySecretInternal;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {
        String gatewaySecret = request.getHeader("X-Gateway-Secret");

        if (gatewaySecret == null || !gatewaySecret.equals(gatewaySecretInternal)) {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            response.setContentType("application/json");
            response.getWriter().write("""
                        {"error":"Invalid Gateway Access"}
                    """);
            return;
        }

        filterChain.doFilter(request, response);
    }
}