package com.example.tpoDA.entities;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;


@Entity
@Table(name = "productos_normales")
@PrimaryKeyJoinColumn(name = "identificador")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class NormalProduct extends Product {

    @Column(name = "nombre", nullable = false, length = 200)
    private String name;

    @Column(name = "marca", length = 150)
    private String brand;

    @Column(name = "cantidad")
    private Integer quantity;

    @Column(name = "esPropietario", length = 2)
    private String isOwnerConfirmed;
}
