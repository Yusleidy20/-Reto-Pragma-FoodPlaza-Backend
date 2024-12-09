package com.example.foodplaza_traceability.infrastructure.configuration.segurity.jwt;



import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
@Component
public class JwtUtil {
    private static final Logger log = LoggerFactory.getLogger(JwtUtil.class);
    private static final String ACCESS_TOKEN_SECRET =
            System.getenv("ACCESS_TOKEN_SECRET") != null
                    ? System.getenv("ACCESS_TOKEN_SECRET")
                    : "S6Do4LIXCN6KGKmbAS8zwVUbAXCIYvqw";
    private static final Long ACCESS_TOKEN_VALIDITY_SECONDS = 86_400L;

    public String generateToken(String username, String role, Long userId) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("role", role);
        claims.put("userId", userId);
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + ACCESS_TOKEN_VALIDITY_SECONDS * 1000))
                .signWith(Keys.hmacShaKeyFor(ACCESS_TOKEN_SECRET.getBytes()), SignatureAlgorithm.HS256)
                .compact();
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(ACCESS_TOKEN_SECRET.getBytes())
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            log.error("Token validation failed: {}", e.getMessage());
            return false;
        }
    }

    public Long getUserIdFromToken(String token) {
        Object userId = Jwts.parserBuilder()
                .setSigningKey(ACCESS_TOKEN_SECRET.getBytes())
                .build()
                .parseClaimsJws(token)
                .getBody()
                .get("userId");
        return userId != null ? Long.valueOf(userId.toString()) : null;
    }

    public String getUsernameFromToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(ACCESS_TOKEN_SECRET.getBytes())
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    public String getRoleFromToken(String token) {
        return (String) Jwts.parserBuilder()
                .setSigningKey(ACCESS_TOKEN_SECRET.getBytes())
                .build()
                .parseClaimsJws(token)
                .getBody()
                .get("role");
    }
}

