package com.example.barber_app.repository;

import com.example.barber_app.model.VerificationToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface VerificationTokenRepository extends  JpaRepository <VerificationToken, Long> {

    Optional<VerificationToken> findByToken(String token);
}
