package com.example.foodplaza.infrastructure.configuration.segurity.jwt;


import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class JwtUtil {

    private static final String ACCESS_TOKEN_SECRET =
            System.getenv("ACCESS_TOKEN_SECRET") != null
                    ? System.getenv("ACCESS_TOKEN_SECRET")
                    : "S6Do4LIXCN6KGKmbAS8zwVUbAXCIYvqw";
    private static final Long ACCESS_TOKEN_VALIDITY_SECONDS = 86_400L;

    private JwtUtil() {
        throw new UnsupportedOperationException("Utility class");
    }

    public static String generateToken(String username, String role, Long userId) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("role", role);
        claims.put("userId", userId); // Agregar el userId al token

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + ACCESS_TOKEN_VALIDITY_SECONDS * 1000))
                .signWith(Keys.hmacShaKeyFor(ACCESS_TOKEN_SECRET.getBytes()), SignatureAlgorithm.HS256)
                .compact();
    }



    // Validar el token
    public static boolean validateToken(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(ACCESS_TOKEN_SECRET.getBytes())
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false; // Token inválido
        }
    }
    public static Long getUserIdFromToken(String token) {
        Object userId = Jwts.parserBuilder()
                .setSigningKey(ACCESS_TOKEN_SECRET.getBytes())
                .build()
                .parseClaimsJws(token)
                .getBody()
                .get("userId");
        return userId != null ? Long.valueOf(userId.toString()) : null;
    }

    // Extraer el usuario del token
    public static String getUsernameFromToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(ACCESS_TOKEN_SECRET.getBytes())
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    // Extraer el rol del token
    public static String getRoleFromToken(String token) {
        return (String) Jwts.parserBuilder()
                .setSigningKey(ACCESS_TOKEN_SECRET.getBytes())
                .build()
                .parseClaimsJws(token)
                .getBody()
                .get("role");
    }
}
