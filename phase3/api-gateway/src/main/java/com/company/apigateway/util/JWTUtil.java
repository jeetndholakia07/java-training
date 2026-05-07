package com.company.apigateway.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.stereotype.Component;

import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

@Component
public class JWTUtil {
    private final PublicKey publicKey;
    public JWTUtil() throws Exception{
        this.publicKey = loadPublicKey(System.getenv("JWT_PUBLIC_KEY"));
    }
    private PublicKey loadPublicKey(String base64) throws Exception {

        String sanitized = base64
                .replaceAll("\\s+", "");

        byte[] keyBytes = Base64.getDecoder().decode(sanitized);

        X509EncodedKeySpec spec =
                new X509EncodedKeySpec(keyBytes);

        return KeyFactory.getInstance("RSA")
                .generatePublic(spec);
    }

    public boolean validateToken(String token){
        try{
            Jwts.parserBuilder()
                    .setSigningKey(publicKey)
                    .build()
                    .parseClaimsJws(token);
            return true;
        }
        catch (Exception e){
            return false;
        }
    }

    public Claims extractClaims(String token){
        return Jwts.parserBuilder()
                .setSigningKey(publicKey)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}
