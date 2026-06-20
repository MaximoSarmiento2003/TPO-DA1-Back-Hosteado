package com.example.tpoDA.controller;

import com.example.tpoDA.dtos.user.ChangePasswordDTO;
import com.example.tpoDA.dtos.user.UserProfileResponseDTO;
import com.example.tpoDA.dtos.user.UserProfileUpdateDTO;
import com.example.tpoDA.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    // =========================
    // GET /users/profile
    // Endpoint 5 - Obtener perfil del usuario autenticado
    // =========================
    @GetMapping("/profile")
    public ResponseEntity<UserProfileResponseDTO> getProfile(Authentication auth) {
        return ResponseEntity.ok(userService.getProfile(auth.getName()));
    }

    // =========================
    // PUT /users/profile
    // Endpoint 32 - Actualizar datos del perfil
    // =========================
    @PutMapping("/profile")
    public ResponseEntity<Map<String, String>> updateProfile(
            @RequestBody UserProfileUpdateDTO dto,
            Authentication auth
    ) {
        return ResponseEntity.ok(userService.updateProfile(dto, auth.getName()));
    }

    // =========================
    // PUT /users/change-password
    // Endpoint 33 - Cambiar contraseña
    // =========================
    @PutMapping("/change-password")
    public ResponseEntity<Map<String, String>> changePassword(
            @RequestBody ChangePasswordDTO dto,
            Authentication auth
    ) {
        return ResponseEntity.ok(userService.changePassword(dto, auth.getName()));
    }
}

