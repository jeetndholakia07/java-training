package com.company.apigateway.config;

import com.company.apigateway.util.JWTUtil;
import io.jsonwebtoken.Claims;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
public class JWTFilter implements GlobalFilter {
    private final JWTUtil jwtUtil;
    private final RouteProperties routeProperties;
    public JWTFilter(JWTUtil jwtUtil, RouteProperties routeProperties){
        this.jwtUtil = jwtUtil;
        this.routeProperties = routeProperties;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
       String path = exchange.getRequest().getPath().toString();
        final String gatewayCode = System.getenv("GATEWAY_SECRET");
       if(isPublic(path)){
           ServerHttpRequest mutatedRequest = exchange.getRequest()
                   .mutate()
                   .headers(httpHeaders ->
                           httpHeaders.add("X-Gateway-Secret",gatewayCode))
                   .build();
           return chain.filter(exchange.mutate()
                   .request(mutatedRequest)
                   .build());
       }
       String authHeader = exchange.getRequest()
               .getHeaders()
               .getFirst(HttpHeaders.AUTHORIZATION);
       if(authHeader==null || !authHeader.startsWith("Bearer ")){
           return unauthorized(exchange);
       }
       String token = authHeader.substring(7);
       if(!jwtUtil.validateToken(token)){
           return unauthorized(exchange);
       }
        Claims claims = jwtUtil.extractClaims(token);
        String role = claims.get("role", String.class);
        String username = claims.get("username", String.class);
        String guid = claims.get("userGuid", String.class);
        if(!hasAccess(path,role)){
            return forbidden(exchange);
        }
        ServerHttpRequest mutatedRequest = exchange.getRequest()
                .mutate()
                .headers(headers -> {
                    headers.remove("X-User");
                    headers.remove("X-ID");
                    headers.remove("X-Role");
                    headers.remove("X-Gateway-Secret");

                    headers.add("X-User", username);
                    headers.add("X-ID", guid);
                    headers.add("X-Role", role);
                    headers.add("X-Gateway-Secret", gatewayCode);
                })
                .build();
        return chain.filter(exchange.mutate()
                .request(mutatedRequest)
                .build()
        );
    }

    private boolean isPublic(String path){
        return routeProperties.getPublicRoutes().stream()
                .anyMatch(path::startsWith);
    }

    private boolean hasAccess(String path, String role){
        if(routeProperties.getRoleRoutes()==null){
            return true;
        }
        return routeProperties.getRoleRoutes().stream().anyMatch(route->
                path.startsWith(route.getPath()) && role.equals(route.getRole()));
    }

    private Mono<Void> unauthorized(ServerWebExchange exchange){
        exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
        return exchange.getResponse().setComplete();
    }
    private Mono<Void> forbidden(ServerWebExchange exchange){
        exchange.getResponse().setStatusCode(HttpStatus.FORBIDDEN);
        return exchange.getResponse().setComplete();
    }
}
