package com.example.tpoDA.services;

import com.example.tpoDA.dtos.admin.ApproveClientDTO;
import com.example.tpoDA.dtos.admin.ClientStatusResponseDTO;
import com.example.tpoDA.dtos.admin.RejectClientDTO;
import com.example.tpoDA.entities.*;
import com.example.tpoDA.exceptions.AppException;
import com.example.tpoDA.repositories.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AdminService {

    private final UserRepository userRepository;
    private final ClientRepository clientRepository;
    private final PendingRegistrationRepository pendingRegistrationRepository;
    private final EmailService emailService;

    // ─── APROBAR ──────────────────────────────────────────────────────────────

    @Transactional
    public ClientStatusResponseDTO approveClient(Integer clientId, ApproveClientDTO dto) {

        Client client = clientRepository.findById(clientId)
                .orElseThrow(() -> new AppException("Cliente no encontrado", HttpStatus.NOT_FOUND));

        User user = userRepository.findByClientId(clientId)
                .orElseThrow(() -> new AppException("Usuario no encontrado para este cliente", HttpStatus.NOT_FOUND));

        // Validar que no esté ya aprobado
        if (RegistrationStatus.APROBADO.equals(user.getRegistrationStatus())) {
            throw new AppException("El cliente ya fue aprobado", HttpStatus.CONFLICT);
        }

        // 1. Actualizar Client
        client.setAdmitted("si");
        ClientCategory category = dto.getCategory() != null ? dto.getCategory() : ClientCategory.COMUN;
        client.setCategory(category);
        clientRepository.save(client);

        // 2. Actualizar estado en User
        user.setRegistrationStatus(RegistrationStatus.APROBADO);
        userRepository.save(user);

        // 3. Generar token de activación (o reusar uno vigente)
        String token = resolveActivationToken(user);

        // 4. Mandar mail
        try {
            emailService.sendSetPasswordEmail(user.getEmail(), token);
        } catch (Exception e) {
            System.err.println("[AdminService] No se pudo enviar mail de aprobación: " + e.getMessage());
            System.err.println("[DEV] Token de activación para " + user.getEmail() + ": " + token);
        }

        return toStatusDTO(client, user);
    }

    // ─── RECHAZAR ────────────────────────────────────────────────────────────

    @Transactional
    public ClientStatusResponseDTO rejectClient(Integer clientId, RejectClientDTO dto) {

        Client client = clientRepository.findById(clientId)
                .orElseThrow(() -> new AppException("Cliente no encontrado", HttpStatus.NOT_FOUND));

        User user = userRepository.findByClientId(clientId)
                .orElseThrow(() -> new AppException("Usuario no encontrado para este cliente", HttpStatus.NOT_FOUND));

        // Validar que no esté ya procesado
        if (RegistrationStatus.APROBADO.equals(user.getRegistrationStatus())) {
            throw new AppException("No se puede rechazar un cliente ya aprobado", HttpStatus.CONFLICT);
        }

        // 1. Client sigue con admitted = "no"
        client.setAdmitted("no");
        clientRepository.save(client);

        // 2. Actualizar estado en User
        user.setRegistrationStatus(RegistrationStatus.RECHAZADO);
        userRepository.save(user);

        // 3. Mandar mail de rechazo
        try {
            emailService.sendRejectionEmail(user.getEmail(), dto.getReason());
        } catch (Exception e) {
            System.err.println("[AdminService] No se pudo enviar mail de rechazo: " + e.getMessage());
        }

        return toStatusDTO(client, user);
    }

    // ─── CONSULTAR ESTADO ────────────────────────────────────────────────────

    public ClientStatusResponseDTO getClientStatus(Integer clientId) {
        Client client = clientRepository.findById(clientId)
                .orElseThrow(() -> new AppException("Cliente no encontrado", HttpStatus.NOT_FOUND));

        User user = userRepository.findByClientId(clientId)
                .orElseThrow(() -> new AppException("Usuario no encontrado", HttpStatus.NOT_FOUND));

        return toStatusDTO(client, user);
    }

    // ─── Helpers ─────────────────────────────────────────────────────────────

    // Busca un token vigente o genera uno nuevo
    private String resolveActivationToken(User user) {
        return pendingRegistrationRepository.findByUserId(user.getId())
                .map(existing -> {
                    if (!existing.isUsed() && !existing.isExpired()) {
                        // Reusar el token existente y extender 48hs
                        existing.setExpiresAt(LocalDateTime.now().plusHours(48));
                        pendingRegistrationRepository.save(existing);
                        return existing.getToken();
                    } else {
                        // Regenerar
                        existing.setToken(UUID.randomUUID().toString());
                        existing.setExpiresAt(LocalDateTime.now().plusHours(48));
                        existing.setUsed(false);
                        pendingRegistrationRepository.save(existing);
                        return existing.getToken();
                    }
                })
                .orElseGet(() -> {
                    // Primera vez que se aprueba — crear el registro pendiente
                    String newToken = UUID.randomUUID().toString();
                    PendingRegistration pending = PendingRegistration.builder()
                            .user(user)
                            .token(newToken)
                            .expiresAt(LocalDateTime.now().plusHours(48))
                            .used(false)
                            .build();
                    pendingRegistrationRepository.save(pending);
                    return newToken;
                });
    }

    private ClientStatusResponseDTO toStatusDTO(Client client, User user) {
        return ClientStatusResponseDTO.builder()
                .clientId(client.getId())
                .email(user.getEmail())
                .name(client.getPerson() != null ? client.getPerson().getName() : null)
                .registrationStatus(user.getRegistrationStatus() != null
                        ? user.getRegistrationStatus().name() : null)
                .admitted(client.getAdmitted())
                .category(client.getCategory() != null ? client.getCategory().name() : null)
                .build();
    }
}
