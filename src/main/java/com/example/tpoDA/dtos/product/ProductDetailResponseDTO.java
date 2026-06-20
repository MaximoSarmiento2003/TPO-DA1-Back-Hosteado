package com.example.tpoDA.dtos.product;

import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class ProductDetailResponseDTO {

    // Datos base
    private Integer id;
    private String type;        // "art" | "normal"
    private String name;
    private String brand;
    private Integer quantity;
    private String catalogDescription;
    private String fullDescription;
    private String history;     // solo art
    private LocalDate date;
    private String available;

    // Fotos como base64
    private List<String> photosBase64;

    // Seguro
    private InsuranceInfo insurance;

    // Depósito
    private DepositoInfo deposito;

    // Revisor (empleado)
    private String reviewerName;

    @Data @NoArgsConstructor @AllArgsConstructor @Builder
    public static class InsuranceInfo {
        private String policyNumber;
        private String company;
        private String combinedPolicy;
        private BigDecimal amount;
    }

    @Data @NoArgsConstructor @AllArgsConstructor @Builder
    public static class DepositoInfo {
        private Integer id;
        private String nombre;
        private String direccion;
        private String ciudad;
        private String pais;
        private String telefono;
        private String responsable;
        private String horario;
    }
}
