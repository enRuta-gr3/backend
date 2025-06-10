package com.uy.enRutaBackend.persistence;

import com.uy.enRutaBackend.entities.PasswordResetToken;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

public interface PasswordResetTokenRepository extends JpaRepository<PasswordResetToken, UUID> {
    PasswordResetToken findByToken(String token);
    void deleteByToken(String token);
}
