package com.example.tpoDA.entities;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "personas")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Person {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "identificador")
    private Integer id;

    @Column(name = "documento", nullable = false, length = 20)
    private String document;

    @Column(name = "nombre", nullable = false, length = 150)
    private String name;

    @Column(name = "direccion", length = 250)
    private String address;

    @Enumerated(EnumType.STRING)
    @Column(name = "estado", length = 15)
    private PersonStatus status;

    @Lob
    @Column(name = "foto",columnDefinition = "LONGBLOB")
    private byte[] photo;
}
