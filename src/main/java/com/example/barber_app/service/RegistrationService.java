package com.example.barber_app.service;

import com.example.barber_app.dto.RegisterForm;
import com.example.barber_app.model.User;
import com.example.barber_app.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class RegistrationService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public void register(RegisterForm form) {
        String username = form.getUsername().trim();

        if (!form.getPassword().equals(form.getConfirmPassword())) {
            throw new IllegalArgumentException("Passwords do not match.");
        }

        if (userRepository.existsByUsername(username)) {
            throw new IllegalArgumentException("Username already in use.");
        }

        User user = User.builder()
                .username(username)
                .passwordHash(passwordEncoder.encode(form.getPassword())) // -> transforming the password in a BCrypt hash
                .role("ROLE_USER")
                .build();

        userRepository.save(user);
    }
}