package com.webshop.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Security configuration for the web shop application.
 * 
 * @author WebShop Team
 * @version 1.0
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable()) // For demo purposes
                .authorizeHttpRequests(authz -> authz
                        .requestMatchers("/api/products/**").permitAll()
                        .requestMatchers("/api/orders/**").authenticated()
                        .requestMatchers("/api/cases/**").authenticated()
                        .anyRequest().authenticated())
                .httpBasic(basic -> {
                });

        return http.build();
    }
}
