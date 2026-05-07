package com.company.authservice.service;

import com.company.authservice.dto.UserPayload;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.stereotype.Service;

import java.security.*;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.Date;

@Service
public class JwtService {
    private final PrivateKey privateKey;
    public JwtService() throws Exception {
        this.privateKey = loadPrivateKey(System.getenv("JWT_PRIVATE_KEY"));
    }
    public String generateToken(UserPayload payload){
        return Jwts.builder()
                .setSubject(String.valueOf(payload.getUserGuid()))
                .claim("userGuid",payload.getUserGuid())
                .claim("username",payload.getUsername())
                .claim("role",payload.getRole())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis()+3600_000))
                .signWith(privateKey, SignatureAlgorithm.RS256)
                .compact();
    }

    private PrivateKey loadPrivateKey(String base64) throws Exception {

        String sanitized = base64
                .replaceAll("\\s+", "");

        byte[] keyBytes = Base64.getDecoder().decode(sanitized);

        PKCS8EncodedKeySpec spec =
                new PKCS8EncodedKeySpec(keyBytes);

        return KeyFactory.getInstance("RSA")
                .generatePrivate(spec);
    }
}
