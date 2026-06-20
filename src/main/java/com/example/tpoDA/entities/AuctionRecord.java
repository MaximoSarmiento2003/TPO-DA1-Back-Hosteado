package com.example.tpoDA.entities;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(name = "registroDeSubasta")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AuctionRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "identificador")
    private Integer id;

    //Subasta
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "subasta", nullable = false)
    private Auction auction;

    //Dueño (vendedor)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "duenio", nullable = false)
    private Owner owner;

    //Producto
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "producto", nullable = false)
    private Product product;

    //Cliente (comprador)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cliente", nullable = false)
    private Client client;

    @Column(name = "importe", nullable = false, precision = 18, scale = 2)
    private BigDecimal amount;

    @Column(name = "comision", nullable = false, precision = 18, scale = 2)
    private BigDecimal commission;
}
