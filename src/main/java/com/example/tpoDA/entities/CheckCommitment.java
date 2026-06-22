package com.example.tpoDA.entities;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;

// Rastrea cuánto de un cheque certificado está comprometido en la oferta vigente
// de un usuario sobre un ítem puntual. Como Bid es una tabla protegida del TP
// y no se le puede agregar la FK al medio de pago, este registro vive aparte.
@Entity
@Table(name = "check_commitments",
       uniqueConstraints = @UniqueConstraint(columnNames = {"payment_method_id", "item_id"}))
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class CheckCommitment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "payment_method_id", nullable = false)
    private PaymentMethod paymentMethod;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id", nullable = false)
    private CatalogItem item;

    @Column(name = "amount", nullable = false, precision = 18, scale = 2)
    private BigDecimal amount;

    // true cuando el ítem ya cerró y este cheque ganó: el compromiso queda firme
    // y no se libera aunque el ítem se cierre.
    @Column(name = "locked", nullable = false)
    @Builder.Default
    private Boolean locked = false;
}
