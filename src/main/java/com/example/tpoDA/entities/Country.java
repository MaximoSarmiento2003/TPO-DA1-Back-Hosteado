package com.example.tpoDA.entities;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "paises")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Country {

    @Id
    @Column(name = "numero")
    private Integer id;

    @Column(name = "nombre", nullable = false, length = 250)
    private String name;

    @Column(name = "nombreCorto", length = 250)
    private String shortName;

    @Column(name = "capital", nullable = false, length = 250)
    private String capital;

    @Column(name = "nacionalidad", nullable = false, length = 250)
    private String nationality;

    @Column(name = "idiomas", nullable = false, length = 150)
    private String languages;
}


