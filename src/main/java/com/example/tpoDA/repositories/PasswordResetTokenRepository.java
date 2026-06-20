package com.example.tpoDA.repositories;

import com.example.tpoDA.entities.PasswordResetToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PasswordResetTokenRepository extends JpaRepository<PasswordResetToken, Integer> {

    Optional<PasswordResetToken> findByToken(String token);

    // Para invalidar tokens anteriores del mismo usuario antes de crear uno nuevo
    void deleteByUserId(Integer userId);
}
