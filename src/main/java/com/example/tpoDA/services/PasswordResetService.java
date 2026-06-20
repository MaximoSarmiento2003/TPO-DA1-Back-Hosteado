package com.example.tpoDA.services;

import com.example.tpoDA.entities.PasswordResetToken;
import com.example.tpoDA.entities.User;
import com.example.tpoDA.exceptions.AppException;
import com.example.tpoDA.repositories.PasswordResetTokenRepository;
import com.example.tpoDA.repositories.UserRepository;
import com.example.tpoDA.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PasswordResetService {

    private final UserRepository userRepository;
    private final PasswordResetTokenRepository passwordResetTokenRepository;
    private final EmailService emailService;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    // ─── PASO 1: Solicitar recuperación ──────────────────────────────────────

    @Transactional
    public void requestReset(String email) {

        // Siempre respondemos OK aunque el email no exista
        // (para no revelar si un email está registrado o no)
        userRepository.findByEmail(email).ifPresent(user -> {

            // Invalidar tokens anteriores del mismo usuario
            passwordResetTokenRepository.deleteByUserId(user.getId());

            // Generar nuevo token, expira en 1 hora
            String token = UUID.randomUUID().toString();
            PasswordResetToken resetToken = PasswordResetToken.builder()
                    .user(user)
                    .token(token)
                    .expiresAt(LocalDateTime.now().plusHours(1))
                    .used(false)
                    .build();
            passwordResetTokenRepository.save(resetToken);

            try {
                emailService.sendPasswordResetEmail(email, token);
            } catch (Exception e) {
                System.err.println("[PasswordResetService] No se pudo enviar mail: " + e.getMessage());
                System.err.println("[DEV] Token de reset para " + email + ": " + token);
            }
        });
    }

    // ─── PASO 2: Validar token (para mostrar el form en el front) ────────────

    public void validateToken(String token) {
        PasswordResetToken resetToken = passwordResetTokenRepository.findByToken(token)
                .orElseThrow(() -> new AppException("El link no es válido", HttpStatus.BAD_REQUEST));

        if (resetToken.isUsed()) {
            throw new AppException("Este link ya fue utilizado", HttpStatus.CONFLICT);
        }
        if (resetToken.isExpired()) {
            throw new AppException("El link expiró. Solicitá uno nuevo.", HttpStatus.GONE);
        }
    }

    // ─── PASO 3: Cambiar contraseña ───────────────────────────────────────────

    @Transactional
    public void resetPassword(String token, String newPassword) {

        if (newPassword == null || newPassword.length() < 6) {
            throw new AppException("La contraseña debe tener al menos 6 caracteres", HttpStatus.BAD_REQUEST);
        }

        PasswordResetToken resetToken = passwordResetTokenRepository.findByToken(token)
                .orElseThrow(() -> new AppException("El link no es válido", HttpStatus.BAD_REQUEST));

        if (resetToken.isUsed()) {
            throw new AppException("Este link ya fue utilizado", HttpStatus.CONFLICT);
        }
        if (resetToken.isExpired()) {
            throw new AppException("El link expiró. Solicitá uno nuevo.", HttpStatus.GONE);
        }

        // Actualizar contraseña
        User user = resetToken.getUser();
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);

        // Marcar token como usado
        resetToken.setUsed(true);
        passwordResetTokenRepository.save(resetToken);
    }
}
