package com.example.foodplaza;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class TestSecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf().disable() // Deshabilitar CSRF para pruebas
                .authorizeRequests()
                .anyRequest().permitAll(); // Permitir todas las solicitudes

        return http.build();
    }
}
