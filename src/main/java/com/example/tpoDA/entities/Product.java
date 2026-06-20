package com.example.tpoDA.entities;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;
import java.util.List;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@Table(name = "productos")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "identificador")
    private Integer id;

    @Column(name = "fecha")
    private LocalDate date;

    // 'si' / 'no'
    @Column(name = "disponible", length = 2)
    private String available;

    @Column(name = "descripcionCatalogo", length = 500)
    private String catalogDescription;

    @Column(name = "descripcionCompleta", nullable = false, length = 300)
    private String fullDescription;

    //Empleado revisor
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "revisor", nullable = false)
    private Employee reviewer;

    //Dueño
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "duenio", nullable = false)
    private Owner owner;

    //Seguro (opcional)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "seguro", referencedColumnName = "nroPoliza")
    private Insurance insurance;

    //Depósito donde se almacena el producto (opcional)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "deposito_id")
    private Deposito deposito;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL)
    private List<Photo> photos;

}
