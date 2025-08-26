package com.example.e_commerce.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.*;

@Component
public class JWTUtil {

    public static final Logger logger = LoggerFactory.getLogger(JWTUtil.class);


    @Value("${app.jwt.secret:mySecretKey12345678901234567890123456789012}")
    private String jwtSecret;

    @Value("${app.jwt.expiration:86400000}") // 24 hours in milliseconds
    private int jwtExpirationMs;

    @Value("${app.jwt.refresh-expiration:604800000}") // 7 days in milliseconds
    private int refreshTokenExpirationMs;

    public String generateTokenFromUserId(String userId, String username, boolean isRefreshToken) {
        Date expiryDate = new Date(System.currentTimeMillis() +
                (isRefreshToken ? refreshTokenExpirationMs : jwtExpirationMs));

        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", userId);
        claims.put("username", username);
        claims.put("type", isRefreshToken ? "refresh" : "access");

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(userId.toString())
                .setIssuedAt(new Date())
                .setExpiration(expiryDate)
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    // Generate refresh token
    public String generateRefreshToken(Authentication authentication) {
        CustomUserDetails userPrincipal = (CustomUserDetails) authentication.getPrincipal();
        return generateTokenFromUserId(userPrincipal.getId(), userPrincipal.getUsername(), true);
    }

    // Get username from JWT token
    public String getUsernameFromJwtToken(String token) {
        Claims claims = Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();

        return claims.get("username", String.class);
    }

    // Get user ID from JWT token
    public String getUserIdFromJwtToken(String token) {
        Claims claims = Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();

        return claims.get("userId", String.class);
    }

    // Validate JWT token
    public boolean validateJwtToken(String authToken) {
        try {
            Jwts.parser()
                    .verifyWith(getSigningKey())
                    .build()
                    .parseSignedClaims(authToken);
            return true;
        } catch (MalformedJwtException e) {
            logger.error("Invalid JWT token: {}", e.getMessage());
        } catch (ExpiredJwtException e) {
            logger.error("JWT token is expired: {}", e.getMessage());
        } catch (UnsupportedJwtException e) {
            logger.error("JWT token is unsupported: {}", e.getMessage());
        } catch (IllegalArgumentException e) {
            logger.error("JWT claims string is empty: {}", e.getMessage());
        } catch (SecurityException e) {
            logger.error("JWT signature validation failed: {}", e.getMessage());
        }
        return false;
    }


    public Claims extractAllClaims(String token) {

        Jws<Claims> claimsJws = Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token);
        return claimsJws.getPayload();

    }

    // Check if token is refresh token
    public boolean isRefreshToken(String token) {
        try {
            Claims claims = Jwts
                    .parser()
                    .verifyWith(getSigningKey())
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();

            return "refresh".equals(claims.get("type", String.class));
        } catch (Exception e) {
            return false;
        }
    }

    // Get expiration date from token
    public Date getExpirationDateFromToken(String token) {
        Claims claims = Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();

        return claims.getExpiration();
    }

    // Check if token is expired
    public boolean isTokenExpired(String token) {
        Date expiration = getExpirationDateFromToken(token);
        return expiration.before(new Date());
    }

    // Get signing key
    private SecretKey getSigningKey() {
        byte[] keyBytes = jwtSecret.getBytes(StandardCharsets.UTF_8);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
