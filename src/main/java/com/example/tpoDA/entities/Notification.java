package com.example.tpoDA.entities;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "notifications")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private Product product;

    // FK a subasta (para notificaciones de subasta)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "auction_id")
    private Auction auction;

    /*
     * Tipos soportados:
     * PRODUCT_APPROVED  — producto aprobado por la empresa
     * PRODUCT_REJECTED  — producto rechazado
     * BID_OUTBID        — tu puja fue superada
     * AUCTION_STARTING  — subasta a punto de empezar
     * PAYMENT_SUCCESS   — pago confirmado
     * WATCHLIST_ALERT   — nuevo ítem en subasta de interés
     */
    @Column(name = "type", length = 30, nullable = false)
    private String type;

    @Column(name = "message", length = 1000, nullable = false)
    private String message;

    // Datos extra para renderizar en el front
    @Column(name = "base_price", precision = 18, scale = 2)
    private BigDecimal basePrice;

    @Column(name = "commission_rate", precision = 5, scale = 2)
    private BigDecimal commissionRate;

    // Importe de puja superada / confirmada
    @Column(name = "amount", precision = 18, scale = 2)
    private BigDecimal amount;

    // Minutos hasta inicio de subasta (para AUCTION_STARTING)
    @Column(name = "minutes_to_start")
    private Integer minutesToStart;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "read_at")
    private LocalDateTime readAt;

    @Column(name = "user_response", length = 20)
    @Builder.Default
    private String userResponse = "PENDING";

    public boolean isRead() { return readAt != null; }
}
