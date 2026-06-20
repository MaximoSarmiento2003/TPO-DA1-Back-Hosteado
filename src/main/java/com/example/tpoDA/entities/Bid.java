package com.example.tpoDA.entities;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(name = "pujos")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Bid {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "identificador")
    private Integer id;

    //Asistente (postor)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "asistente", nullable = false)
    private Attendee attendee;

    //Item de catálogo (lote)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item", nullable = false)
    private CatalogItem item;

    @Column(name = "importe", nullable = false, precision = 18, scale = 2)
    private BigDecimal amount;

    // 'si' / 'no'
    @Column(name = "ganador", length = 2)
    private String winner;
}

