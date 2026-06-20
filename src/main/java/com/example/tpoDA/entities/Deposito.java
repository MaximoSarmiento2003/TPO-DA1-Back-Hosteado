package com.example.tpoDA.entities;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "depositos")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Deposito {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "nombre", nullable = false, length = 150)
    private String nombre;

    @Column(name = "direccion", nullable = false, length = 300)
    private String direccion;

    @Column(name = "ciudad", length = 100)
    private String ciudad;

    @Column(name = "pais", length = 100)
    private String pais;

    @Column(name = "telefono", length = 30)
    private String telefono;

    @Column(name = "responsable", length = 150)
    private String responsable;

    @Column(name = "horario", length = 100)
    private String horario;
}
