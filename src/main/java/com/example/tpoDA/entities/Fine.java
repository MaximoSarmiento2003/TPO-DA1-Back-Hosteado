package com.example.tpoDA.entities;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "multas")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Fine {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "identificador")
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cliente", nullable = false)
    private Client client;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pujo", nullable = false)
    private Bid bid;

    @Column(name = "importe", nullable = false, precision = 18, scale = 2)
    private BigDecimal amount;

    @Column(name = "fechaLimitePago", nullable = false)
    private LocalDate dueDate;

    @Column(name = "estado", length = 15, nullable = false)
    @Builder.Default
    private String status = "pendiente";
}