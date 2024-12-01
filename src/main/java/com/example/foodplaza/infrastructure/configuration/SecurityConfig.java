package com.example.foodplaza.infrastructure.configuration;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.firewall.HttpFirewall;
import org.springframework.security.web.firewall.StrictHttpFirewall;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(request -> request
                        // Permitir acceso sin autenticación a estas rutas específicas
                        .requestMatchers("/user-micro/foodplaza/**").permitAll()
                        // Rutas protegidas que requieren autenticación
                        .anyRequest().authenticated()
                )
                .csrf(csrf -> csrf.disable()); // Deshabilitar CSRF si no es necesario

        return http.build();
    }

    @Bean
    public HttpFirewall httpFirewall() {
        StrictHttpFirewall firewall = new StrictHttpFirewall();
        // Permitir secuencias codificadas específicas
        firewall.setAllowUrlEncodedPercent(true); // Habilita el uso de %
        firewall.setAllowUrlEncodedPeriod(true); // Habilita '.'
        firewall.setAllowUrlEncodedSlash(true);  // Habilita '/'
        firewall.setAllowBackSlash(true);        // Habilita '\'
        return firewall;
    }
}

