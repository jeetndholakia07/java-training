package com.company.apigateway.config;

import com.company.apigateway.util.JWTUtil;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.List;

@Component
public class JWTFilter implements GlobalFilter {
    private final JWTUtil jwtUtil;

    @Value("${GATEWAY_SECRET}")
    private String gatewaySecret;

    private static final List<String> PUBLIC_ROUTES = List.of(
            "/v1/auth/login",
            "/v1/auth/register"
    );

    public JWTFilter(JWTUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Override
    public Mono<Void> filter(
            ServerWebExchange exchange,
            GatewayFilterChain chain
    ) {
        String path = exchange.getRequest()
                .getURI()
                .getPath();

        if (isPublicRoute(path)) {
            return chain.filter(
                    addGatewayHeader(exchange)
            );
        }

        String authHeader = exchange.getRequest()
                .getHeaders()
                .getFirst(HttpHeaders.AUTHORIZATION);

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return unauthorized(exchange);
        }

        String token = authHeader.substring(7);

        if (!jwtUtil.validateToken(token)) {
            return unauthorized(exchange);
        }

        Claims claims = jwtUtil.extractClaims(token);

        return chain.filter(
                addUserHeaders(
                        exchange,
                        claims.get("username", String.class),
                        claims.get("userGuid", String.class),
                        claims.get("role", String.class)
                )
        );
    }

    private boolean isPublicRoute(String path) {
        return PUBLIC_ROUTES.stream()
                .anyMatch(path::startsWith);
    }

    private ServerWebExchange addGatewayHeader(
            ServerWebExchange exchange
    ) {
        ServerHttpRequest request = exchange.getRequest()
                .mutate()
                .headers(headers ->
                        headers.set("X-Gateway-Secret", gatewaySecret)
                )
                .build();

        return exchange.mutate()
                .request(request)
                .build();
    }

    private ServerWebExchange addUserHeaders(
            ServerWebExchange exchange,
            String username,
            String userGuid,
            String role
    ) {
        ServerHttpRequest request = exchange.getRequest()
                .mutate()
                .headers(headers -> {
                    headers.set("X-User", username);
                    headers.set("X-ID", userGuid);
                    headers.set("X-Role", role);
                    headers.set("X-Gateway-Secret", gatewaySecret);
                })
                .build();

        return exchange.mutate()
                .request(request)
                .build();
    }

    private Mono<Void> unauthorized(
            ServerWebExchange exchange
    ) {
        exchange.getResponse()
                .setStatusCode(HttpStatus.UNAUTHORIZED);

        return exchange.getResponse()
                .setComplete();
    }
}
