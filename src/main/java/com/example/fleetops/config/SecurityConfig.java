package com.example.fleetops.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .cors(cors -> cors.disable())
                .authorizeHttpRequests(auth -> auth
                        // Permitem accesul liber la autentificare, interfață și API-ul de flotă
                        .requestMatchers(
                                "/api/auth/**",
                                "/api/**",           // <--- NOU: Permite rutele din FleetController
                                "/index.html",
                                "/ws/**",
                                "/swagger-ui/**",
                                "/v3/api-docs/**"
                        ).permitAll()
                        .anyRequest().authenticated()
                )
                .httpBasic(Customizer.withDefaults());

        return http.build();
    }

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        // Ignorăm complet securitatea pentru aceste rute (evităm filtrele interne)
        return (web) -> web.ignoring().requestMatchers(
                "/api/auth/**",
                "/api/**",               // <--- NOU: Foarte important pentru POST /api/orders
                "/index.html",
                "/ws/**",
                "/swagger-ui/**",
                "/v3/api-docs/**"
        );
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}