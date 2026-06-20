package com.example.tpoDA.controller;

import com.example.tpoDA.dtos.fine.FineResponseDTO;
import com.example.tpoDA.dtos.fine.PayFineResponseDTO;
import com.example.tpoDA.services.FineService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users/fines")
@RequiredArgsConstructor
public class FineController {

    private final FineService fineService;

    @GetMapping
    public ResponseEntity<List<FineResponseDTO>> getMyFines(Authentication auth) {
        return ResponseEntity.ok(fineService.getMyFines(auth.getName()));
    }

    @GetMapping("/pending")
    public ResponseEntity<List<FineResponseDTO>> getMyPendingFines(Authentication auth) {
        return ResponseEntity.ok(fineService.getMyPendingFines(auth.getName()));
    }

    @PostMapping("/{fineId}/pay")
    public ResponseEntity<PayFineResponseDTO> payFine(
            @PathVariable Integer fineId,
            Authentication auth
    ) {
        return ResponseEntity.ok(fineService.payFine(fineId, auth.getName()));
    }
}