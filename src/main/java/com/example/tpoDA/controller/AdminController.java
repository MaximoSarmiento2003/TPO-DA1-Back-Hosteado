package com.example.tpoDA.controller;

import com.example.tpoDA.dtos.admin.ApproveClientDTO;
import com.example.tpoDA.dtos.admin.ClientStatusResponseDTO;
import com.example.tpoDA.dtos.admin.RejectClientDTO;
import com.example.tpoDA.services.AdminService;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin/clients")
@RequiredArgsConstructor
public class AdminController {

    private final AdminService adminService;

    // GET /admin/clients/{clientId}/status — ver estado actual
    @GetMapping("/{clientId}/status")
    public ResponseEntity<ClientStatusResponseDTO> getStatus(
            @PathVariable Integer clientId
    ) {
        return ResponseEntity.ok(adminService.getClientStatus(clientId));
    }

    // POST /admin/clients/{clientId}/approve — aprobar y mandar mail con link
    @PostMapping("/{clientId}/approve")
    public ResponseEntity<ClientStatusResponseDTO> approve(
            @PathVariable Integer clientId,
            @RequestBody(required = false) ApproveClientDTO dto
    ) {
        return ResponseEntity.ok(
                adminService.approveClient(clientId, dto != null ? dto : new ApproveClientDTO())
        );
    }

    // POST /admin/clients/{clientId}/reject — rechazar y mandar mail de rechazo
    @PostMapping("/{clientId}/reject")
    public ResponseEntity<ClientStatusResponseDTO> reject(
            @PathVariable Integer clientId,
            @RequestBody(required = false) RejectClientDTO dto
    ) {
        return ResponseEntity.ok(
                adminService.rejectClient(clientId, dto != null ? dto : new RejectClientDTO())
        );
    }

   
}
