package com.example.tpoDA.entities;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(name = "medios_pago")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PaymentMethod {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id", nullable = false)
    private User user;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo", nullable = false, length = 25)
    private PaymentMethodTipo tipo;

    // Tarjeta
    @Column(name = "titular", length = 150)
    private String titular;

    @Column(name = "marca", length = 30)
    private String marca;

    @Column(name = "ultimos4", length = 4)
    private String ultimos4;

    @Column(name = "fecha_expiracion", length = 10)
    private String fechaExpiracion;

    // Cuenta bancaria
    @Column(name = "banco", length = 100)
    private String banco;

    @Column(name = "cbu", length = 25)
    private String cbu;

    // Cheque
    @Column(name = "monto_garantizado", precision = 18, scale = 2)
    private BigDecimal montoGarantizado;

    @Column(name = "moneda", length = 5)
    private String moneda;

    // Estado
    @Column(name = "verificado")
    private Boolean verificado;

    @Column(name = "estado", length = 30)
    private String estado;
}
