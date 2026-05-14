package com.company.inventoryservice.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableMethodSecurity
public class SecurityConfig {
    @Bean
    public GatewayFilter gatewayFilter() {
        return new GatewayFilter();
    }

    @Bean
    public AuthFilter authFilter() {
        return new AuthFilter();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(
            HttpSecurity http,
            GatewayFilter gatewayFilter,
            AuthFilter authFilter
    ) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .formLogin(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable)
                .logout(AbstractHttpConfigurer::disable)

                .authorizeHttpRequests(auth -> auth
                        .anyRequest().authenticated()
                )

                .addFilterBefore(
                        gatewayFilter,
                        UsernamePasswordAuthenticationFilter.class
                )

                .addFilterBefore(
                        authFilter,
                        UsernamePasswordAuthenticationFilter.class
                );

        return http.build();
    }
}