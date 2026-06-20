package com.example.tpoDA.dtos.payment;

import lombok.*;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaymentMethodCreateDTO {

    // Obligatorio siempre
    private String tipo; // TARJETA_CREDITO | CUENTA_BANCARIA | CHEQUE_CERTIFICADO

    // Tarjeta de crédito
    private String titular;
    private String numeroTarjeta;
    private String fechaExpiracion;
    private String cvv;
    private String marca;

    // Cuenta bancaria
    private String banco;
    private String cbu;
    private String pais;

    // Cheque certificado
    private BigDecimal montoGarantizado;
    private String moneda;
}
