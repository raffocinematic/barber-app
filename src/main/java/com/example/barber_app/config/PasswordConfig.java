package com.example.barber_app.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * PasswordConfig : provides a PasswordEncoder bean for the entire application.
 * -------------------------------------------------------------
 * - Password encoding is a cross-cutting security concern.
 * - It should NOT be placed inside SecurityConfig to avoid circular dependencies (for example between SecurityConfig and UserService).
 * - By defining it in a separate @Configuration class, we keep responsibilities clean and modular.
 *  -------------------------------------------------------------
 *  * BCryptPasswordEncoder:
 *          *
 *          * - Uses the BCrypt hashing algorithm.
 *          * - Designed specifically for securely storing passwords.
 *          * - Automatically generates a random salt for each password.
 *          * - Resistant to rainbow table attacks.
 *          * - Computationally expensive by design (slows brute force attacks).
 *          *
 *          * Important:
 *          * - Passwords are NEVER stored in plain text.
 *          * - Only the hashed version is saved in the database.
 *          *
 *          * During login:
 *          * - Spring compares the raw password entered by the user
 *          *   with the stored hash using this encoder.
 */

@Configuration
public class PasswordConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}