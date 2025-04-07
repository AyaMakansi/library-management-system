package com.example.demo.security.filter;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

import java.security.SecureRandom;
import java.util.Date;

import javax.crypto.SecretKey;
import org.springframework.stereotype.Component;

@Component
public class JwtUtil {

    private static final SecretKey secretKey = generateSecretKey();
    private static final long ACCESS_TOKEN_EXPIRATION = 1000 * 60 * 15; // 15 minutes
    private static final long REFRESH_TOKEN_EXPIRATION = 1000 * 60 * 60 * 24 * 7; // 7 days

    // Method to generate a random 256-bit key
    private static SecretKey generateSecretKey() {
        SecureRandom secureRandom = new SecureRandom();
        byte[] keyBytes = new byte[32]; // 32 bytes for 256-bit key
        secureRandom.nextBytes(keyBytes);
        return Keys.hmacShaKeyFor(keyBytes); // Generate a secret key for HMAC-SHA256
    }

    public String generateToken(String username, String role) {

        var result = Jwts.builder()
                .subject(username) // Set subject
                .claim("role", role)
                .issuedAt(new Date()) // Issue date
                .expiration(new Date(System.currentTimeMillis() + ACCESS_TOKEN_EXPIRATION))
                .signWith(secretKey) // Sign with the key
                .compact();
        return result;
    }

    // Generate Refresh Token
    public String generateRefreshToken(String username, String role) {
        return Jwts.builder()
                .subject(username)
                .claim("role", role)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + REFRESH_TOKEN_EXPIRATION))
                .signWith(secretKey)
                .compact();
    }

    public String extractUsername(String token) {
        return getClaims(token).getSubject();
    }

    public boolean validateToken(String token) {
        return !isTokenExpired(token);
    }

    public boolean isTokenExpired(String token) {
        return getClaims(token).getExpiration().before(new Date());
    }

    private Claims getClaims(String token) {
        // Correct usage of JwtParserBuilder to build a JwtParser
        JwtParser jwtParser = Jwts.parser() // JwtParserBuilder
                .verifyWith(secretKey) // Set the signing key
                .build(); // Build the JwtParser

        return jwtParser.parseSignedClaims(token) // Parse JWT and get Claims
                .getPayload(); // Return the Claims body
    }
}
