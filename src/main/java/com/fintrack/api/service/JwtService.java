package com.fintrack.api.service;

import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

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

    private SecretKey getSigningKey() {
        return Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    }

    public String generateToken(
            Long userId, String email) {
        long nowMillis = System.currentTimeMillis();
        Date now = new Date(nowMillis);
        Date expireDate = new Date(nowMillis + expiration);


        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", userId);
        claims.put("sub", email);
        claims.put("iat", now);
        claims.put("exp", expireDate);

        return Jwts.builder()
                .claims(claims)
                .signWith(getSigningKey())
                .compact();
    }

    public Long extractUserId(String token) {
        Claims claims = extractAllClaims(token);
        Object userIdClaim = claims.get("userId");
        if (userIdClaim instanceof Integer) {
            return ((Integer) userIdClaim).longValue();
        }
        if (userIdClaim instanceof Long) {
            return (Long) userIdClaim;
        }
        return Long.valueOf(userIdClaim.toString());

    }

    public String extractEmail(String token) {
        return extractAllClaims(token).getSubject();
    }


    public boolean isTokenValid(String token) {
        try {
            Claims claims = extractAllClaims(token);
            Date expiration = claims.getExpiration();
            if(expiration == null) return false;
            return !expiration.before(new Date());
        } catch (Exception e) {
            // System.err.println("Token validation error: " + e.getMessage());
            return false;
        }
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
}
