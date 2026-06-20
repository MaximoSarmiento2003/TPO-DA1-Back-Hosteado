package com.example.tpoDA.entities;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(name = "itemsCatalogo")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CatalogItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "identificador")
    private Integer id;

    // 🔗 Catálogo
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "catalogo", nullable = false)
    private Catalog catalog;

    // 🔗 Producto
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "producto", nullable = false)
    private Product product;

    @Column(name = "precioBase", nullable = false, precision = 18, scale = 2)
    private BigDecimal basePrice;

    @Column(name = "comision", nullable = false, precision = 18, scale = 2)
    private BigDecimal commission;

    // 'si' / 'no'
    @Column(name = "subastado", length = 2)
    private String auctioned;
}
