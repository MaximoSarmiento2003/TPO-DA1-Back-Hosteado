package com.example.tpoDA.entities;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(name = "seguros")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Insurance {

    @Id
    @Column(name = "nroPoliza", length = 30)
    private String policyNumber;

    @Column(name = "compania", nullable = false, length = 150)
    private String company;

    @Column(name = "polizaCombinada", length = 2)
    private String combinedPolicy;

    @Column(name = "importe", nullable = false, precision = 18, scale = 2)
    private BigDecimal amount;
}
