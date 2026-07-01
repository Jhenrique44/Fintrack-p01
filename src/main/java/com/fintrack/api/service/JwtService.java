package com.fintrack.api.service;

import java.nio.charset.StandardCharsets;
import java.util.Date;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

@Service
public class JwtService {
    
    @Value("${jwt.secret}")
    private String secret; 

    @Value("${jwt.expiration}")
    private long expiration;

    private SecretKey getSigningKey(){ 
        return Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    }

    public String generateToken(
        Long userId, String email
    ) { 
        Date now = new Date();
        Date expireDate = new Date(now.getTime() + expiration);

        return Jwts.builder()
                .subject(email)
                .claim("userId", userId)
                .issuedAt(expireDate)
                .signWith(getSigningKey())
                .compact();
    }

    public Long extractUserId(String token) { 
        Claims claims = extractAllClaims(token);
        return claims.get("userId", Long.class);
    } 

    public String extractEmail(String token) { 
        return extractAllClaims(token).getSubject();
    }

    public boolean isTokenValid(String token) { 
        try {
            Claims claims = extractAllClaims(token); 
            return !claims.getExpiration().before(new Date());
        } catch (Exception e) {
            return false;
        }
    }
    private Claims extractAllClaims(String token){ 
        return Jwts.parser()
                    .verifyWith(getSigningKey())
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
    }
}
