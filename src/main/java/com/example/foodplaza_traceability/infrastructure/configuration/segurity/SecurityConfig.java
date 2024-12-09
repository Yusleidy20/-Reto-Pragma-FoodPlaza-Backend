package com.example.foodplaza_traceability.infrastructure.configuration.segurity;


import com.example.foodplaza_traceability.infrastructure.configuration.segurity.jwt.JwtRequestFilter;
import com.example.foodplaza_traceability.infrastructure.configuration.segurity.jwt.JwtUtil;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;


@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
public class SecurityConfig {

    private final JwtUtil jwtUtil;

    public SecurityConfig(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/traceability/orders/**").hasAuthority("ROLE_Customer")
                        .requestMatchers("/efficiency/orders/**", "/efficiency/employees/**").hasAuthority("ROLE_Owner")
                        .requestMatchers("/traceability/**").permitAll()
                        .requestMatchers("/error/**").permitAll()
                        .anyRequest().authenticated()
                )
                .addFilterBefore(jwtRequestFilter(), UsernamePasswordAuthenticationFilter.class)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .build();
    }

    @Bean
    public JwtRequestFilter jwtRequestFilter() {
        return new JwtRequestFilter(jwtUtil);
    }
}
