package com.example.tpoDA.dtos.payment;

import lombok.*;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaymentMethodResponseDTO {

    private Integer id;
    private String tipo;

    // Tarjeta
    private String marca;
    private String ultimos4;
    private String titular;

    // Cuenta bancaria
    private String banco;
    private String cbu;

    // Cheque
    private BigDecimal montoGarantizado;
    private String moneda;

    private Boolean verificado;
}
