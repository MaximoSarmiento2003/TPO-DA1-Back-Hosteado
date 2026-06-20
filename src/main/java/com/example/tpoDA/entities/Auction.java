package com.example.tpoDA.entities;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Entity
@Table(name = "subastas")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Auction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "identificador")
    private Integer id;

    @Column(name = "fecha")
    private LocalDate date;

    @Column(name = "hora", nullable = false)
    private LocalTime time;

    @Enumerated(EnumType.STRING)
    @Column(name = "estado", length = 10)
    private AuctionStatus status;

    //Subastador
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "subastador")
    private Auctioneer auctioneer;

    @Column(name = "ubicacion", length = 350)
    private String location;

    @Column(name = "capacidadAsistentes")
    private Integer capacity;

    // 'si' / 'no'
    @Column(name = "tieneDeposito", length = 2)
    private String hasWarehouse;

    @Column(name = "seguridadPropia", length = 2)
    private String privateSecurity;

    @Enumerated(EnumType.STRING)
    @Column(name = "categoria", length = 10)
    private ClientCategory category;

    @OneToMany(mappedBy = "auction")
    private List<Catalog> catalogs;

}
