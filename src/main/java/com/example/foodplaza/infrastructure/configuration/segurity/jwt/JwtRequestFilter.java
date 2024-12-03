package com.example.foodplaza.infrastructure.configuration.segurity.jwt;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;
@AllArgsConstructor
public class JwtRequestFilter extends OncePerRequestFilter {
    private static final Logger log = LoggerFactory.getLogger(JwtRequestFilter.class);

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String authorizationHeader = request.getHeader("Authorization");

        String token = null;
        String username = null;
        Long userId = null;

        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            token = authorizationHeader.substring(7);
            username = JwtUtil.getUsernameFromToken(token);
            userId = JwtUtil.getUserIdFromToken(token); // Extraer el userId

            log.info("Username from token: {}", username);
            log.info("UserId from token: {}", userId);
            log.info("Role from token: {}", JwtUtil.getRoleFromToken(token));
        }

        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null && JwtUtil.validateToken(token)) {
            String role = JwtUtil.getRoleFromToken(token);

            var authorities = List.of(new SimpleGrantedAuthority(role));

            var authenticationToken = new UsernamePasswordAuthenticationToken(username, null, authorities);
            authenticationToken.setDetails(userId); // Adjuntar el userId

            SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        }

        filterChain.doFilter(request, response);
    }
}
