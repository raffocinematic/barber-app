package com.example.barber_app.service;

import com.example.barber_app.model.User;
import com.example.barber_app.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;

    /**
     * When calling the login, Spring security calls automatically loadUserByUsername(...)
     * And you return to it an UserDetails with username, hashed password and role.
     * Spring makes a confrontation between inserted password and the hashed one
     *
     *
     * @param username
     * @return
     * @throws UsernameNotFoundException
     */

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username.trim())
                .orElseThrow(() -> new UsernameNotFoundException("User not found."));

        return org.springframework.security.core.userdetails.User
                .withUsername(user.getUsername())
                .password(user.getPasswordHash())
                .roles(user.getRole().replace("ROLE_", ""))
                .build();
    }
}