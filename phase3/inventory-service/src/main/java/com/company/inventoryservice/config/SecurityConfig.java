package com.company.inventoryservice.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
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
                .csrf(csrf -> csrf.disable())
                .formLogin(form -> form.disable())
                .httpBasic(basic -> basic.disable())
                .logout(logout -> logout.disable())

                .authorizeHttpRequests(auth -> auth

                        .requestMatchers(
                                "/v1/auth/login",
                                "/v1/auth/register"
                        ).permitAll()

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