package com.company.productservice.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Component
public class GlobalFilter extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        final String gatewayInternal = System.getenv("GATEWAY_SECRET");
        String gatewaySecret = request.getHeader("X-Gateway-Secret");
        if(gatewaySecret==null || !gatewaySecret.equals(gatewayInternal)){
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            response.setContentType("application/json");
            Map<String,String> res = new HashMap<>();
            res.put("message","Access Forbidden");
            response.getWriter()
                    .write(res.toString());
            return;
        }
        filterChain.doFilter(request, response);
    }
}

