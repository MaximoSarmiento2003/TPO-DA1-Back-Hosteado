package com.example.tpoDA.controller;

import com.example.tpoDA.dtos.notification.NotificationResponseDTO;
import com.example.tpoDA.dtos.notification.UserProductResponseDTO;
import com.example.tpoDA.dtos.product.ProductApproveDTO;
import com.example.tpoDA.dtos.product.ProductRejectDTO;
import com.example.tpoDA.dtos.product.ProductResponseDTO;
import com.example.tpoDA.services.ProductReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class ProductReviewController {

    private final ProductReviewService productReviewService;

    // ─── Admin: listar pendientes ─────────────────────────────────────────────
    @GetMapping("/admin/products/pending")
    public ResponseEntity<List<ProductResponseDTO>> getPending() {
        return ResponseEntity.ok(productReviewService.getPendingProducts());
    }

    // ─── Admin: aprobar ───────────────────────────────────────────────────────
    @PostMapping("/admin/products/{productId}/approve")
    public ResponseEntity<NotificationResponseDTO> approve(
            @PathVariable Integer productId,
            @RequestBody ProductApproveDTO dto
    ) {
        return ResponseEntity.ok(productReviewService.approveProduct(productId, dto));
    }

    // ─── Admin: rechazar ──────────────────────────────────────────────────────
    @PostMapping("/admin/products/{productId}/reject")
    public ResponseEntity<NotificationResponseDTO> reject(
            @PathVariable Integer productId,
            @RequestBody(required = false) ProductRejectDTO dto
    ) {
        return ResponseEntity.ok(productReviewService.rejectProduct(
                productId, dto != null ? dto : new ProductRejectDTO()));
    }

    // ─── Usuario: aceptar o rechazar las condiciones ──────────────────────────
    @PostMapping("/notifications/{notificationId}/respond")
    public ResponseEntity<NotificationResponseDTO> respond(
            @PathVariable Integer notificationId,
            @RequestBody UserProductResponseDTO dto,
            Authentication auth
    ) {
        return ResponseEntity.ok(productReviewService.respondToApproval(
                notificationId, dto.getResponse(), auth.getName()));
    }
}
