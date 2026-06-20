package com.example.tpoDA.controller;

import com.example.tpoDA.dtos.payment.PaymentMethodCreateDTO;
import com.example.tpoDA.dtos.payment.PaymentMethodResponseDTO;
import com.example.tpoDA.services.PaymentMethodService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/payment-methods")
@RequiredArgsConstructor
public class PaymentMethodController {

    private final PaymentMethodService paymentMethodService;

    // =========================
    // GET /payment-methods
    // Endpoint 6 - Obtener medios de pago del usuario
    // =========================
    @GetMapping
    public ResponseEntity<List<PaymentMethodResponseDTO>> getAll(Authentication auth) {
        return ResponseEntity.ok(paymentMethodService.getAll(auth.getName()));
    }

    // =========================
    // POST /payment-methods
    // Endpoint 7 - Agregar medio de pago
    // =========================
    @PostMapping
    public ResponseEntity<?> add(
            @RequestBody PaymentMethodCreateDTO dto,
            Authentication auth
    ) {
        PaymentMethodResponseDTO response = paymentMethodService.add(dto, auth.getName());
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(Map.of(
                        "message", "Medio de pago registrado",
                        "estado", "PENDIENTE_VERIFICACION"
                ));
    }

    // =========================
    // DELETE /payment-methods/{id}
    // Endpoint 8 - Eliminar medio de pago
    // =========================
    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> delete(
            @PathVariable Integer id,
            Authentication auth
    ) {
        paymentMethodService.delete(id, auth.getName());
        return ResponseEntity.ok(Map.of("message", "Medio de pago eliminado"));
    }
}