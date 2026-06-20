package com.example.tpoDA.controller;

import com.example.tpoDA.dtos.login.AuthResponseDTO;
import com.example.tpoDA.dtos.login.LoginRequestDTO;
import com.example.tpoDA.dtos.password.ForgotPasswordRequestDTO;
import com.example.tpoDA.dtos.password.ResetPasswordRequestDTO;
import com.example.tpoDA.dtos.register.RegisterRequestDTO;
import com.example.tpoDA.dtos.register.RegisterResponseDTO;
import com.example.tpoDA.dtos.register.SetPasswordRequestDTO;
import com.example.tpoDA.services.AuthService;
import com.example.tpoDA.services.PasswordResetService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final PasswordResetService passwordResetService;

    @PostMapping("/login")
    public ResponseEntity<AuthResponseDTO> login(@RequestBody LoginRequestDTO request) {
        return ResponseEntity.ok(authService.login(request));
    }

    @PostMapping("/register")
    public ResponseEntity<RegisterResponseDTO> register(@RequestBody RegisterRequestDTO request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(authService.register(request));
    }

    // Activación de cuenta nueva (desde aprobación)
    @PostMapping("/set-password")
    public ResponseEntity<AuthResponseDTO> setPassword(@RequestBody SetPasswordRequestDTO request) {
        return ResponseEntity.ok(authService.setPassword(request));
    }

    // ─── Recuperación de contraseña ───────────────────────────────────────────

    // Paso 1: el usuario ingresa su email
    @PostMapping("/forgot-password")
    public ResponseEntity<Map<String, String>> forgotPassword(
            @RequestBody ForgotPasswordRequestDTO request
    ) {
        passwordResetService.requestReset(request.getEmail());
        // Siempre 200 para no revelar si el email existe
        return ResponseEntity.ok(Map.of(
                "message", "Si el correo está registrado, recibirás un link para restablecer tu contraseña."
        ));
    }

    // Paso 2: validar token antes de mostrar el form (GET)
    @GetMapping("/reset-password/validate")
    public ResponseEntity<Map<String, String>> validateResetToken(
            @RequestParam String token
    ) {
        passwordResetService.validateToken(token);
        return ResponseEntity.ok(Map.of("message", "Token válido"));
    }

    // Paso 3: nueva contraseña
    @PostMapping("/reset-password")
    public ResponseEntity<Map<String, String>> resetPassword(
            @RequestBody ResetPasswordRequestDTO request
    ) {
        passwordResetService.resetPassword(request.getToken(), request.getNewPassword());
        return ResponseEntity.ok(Map.of("message", "Contraseña actualizada correctamente."));
    }
}
