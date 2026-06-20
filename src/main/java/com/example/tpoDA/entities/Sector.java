package com.example.tpoDA.entities;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "sectores")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Sector {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "identificador")
    private Integer id;

    @Column(name = "nombreSector", nullable = false, length = 150)
    private String name;

    @Column(name = "codigoSector", length = 10)
    private String code;

    //FK → Employee
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "responsableSector")
    private Employee responsible;
}
