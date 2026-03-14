package com.example.barber_app.config;

/**
 * SecurityConfig
 * ---------------------------------------------------------
 * Purpose:
 * - This class defines the application's "security rules":
 * which URLs are public, which require authentication,
 * and how login/logout should work.
 * ---------------------------------------------------------
 * Why keep it focused:
 * - A clean SecurityConfig should describe security behavior only
 * (authorization rules, login/logout, CSRF, session policy, etc.).
 * - Avoid putting business logic here to prevent tight coupling and
 * circular dependencies (common when injecting services/encoders).
 */


/**
 * Declares a SecurityFilterChain bean.
 * ---------------------------------------------------------
 * In Spring Security, the "filter chain" is a sequence of servlet filters
 * that intercept every HTTP request before it reaches your controllers.
 * Those filters:
 * - check authentication (is the user logged in?)
 * - enforce authorization (can the user access this URL?)
 * - handle login forms, logout, CSRF tokens, sessions, etc.
 * ---------------------------------------------------------
 * HttpSecurity is a fluent builder provided by Spring Security to configure
 * those behaviors.
 */

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/login", "/register", "/css/**", "/js/**", "/images/**").permitAll()
                        .anyRequest().authenticated()
                )
                .formLogin(form -> form
                        .loginPage("/login")
                        //this tells to Spring Security where do you go after logging in
                        .defaultSuccessUrl("/home", true)
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutSuccessUrl("/login?logout")
                        .permitAll()
                );

        return http.build();
    }
}
