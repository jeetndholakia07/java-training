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

    public String generateToken(UserPayload payload) {
        return Jwts.builder()
                .setSubject(String.valueOf(payload.userGuid()))
                .claim("userGuid", payload.userGuid())
                .claim("username", payload.username())
                .claim("role", payload.role())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 60 * 60 * 1000))
                .signWith(privateKey, SignatureAlgorithm.RS256)
                .compact();
    }

    private PrivateKey loadPrivateKey(String envValue) throws Exception {
        String pem = new String(
                Base64.getDecoder().decode(envValue)
        );

        String sanitized = pem
                .replace("-----BEGIN PRIVATE KEY-----", "")
                .replace("-----END PRIVATE KEY-----", "")
                .replaceAll("\\s+", "");

        byte[] keyBytes = Base64.getDecoder().decode(sanitized);

        PKCS8EncodedKeySpec spec =
                new PKCS8EncodedKeySpec(keyBytes);

        return KeyFactory.getInstance("RSA").generatePrivate(spec);
    }
}
