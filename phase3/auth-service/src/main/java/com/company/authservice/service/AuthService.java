package com.company.authservice.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class AuthService {
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    public AuthService(PasswordEncoder encoder, JwtService jwtService){
        this.passwordEncoder = encoder;
        this.jwtService = jwtService;
    }
    public String hashPassword(String password){
        return passwordEncoder.encode(password);
    }
    public boolean verifyPassword(String raw, String hash){
        return passwordEncoder.matches(raw, hash);
    }
    public String generateUUID(){
        return UUID.randomUUID().toString();
    }
    public JwtService getJwtService() {
        return jwtService;
    }
}
