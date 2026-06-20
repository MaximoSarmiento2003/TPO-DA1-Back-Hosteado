package com.example.tpoDA.entities;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "clientes")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Client {

    @Id
    @Column(name = "identificador")
    private Integer id;

    //1 a 1 con Person (PK compartida)
    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    @JoinColumn(name = "identificador")
    private Person person;

    //País
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "numeroPais")
    private Country country;

    //Empleado verificador
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "verificador", nullable = false)
    private Employee verifier;

    // CHECK ('si','no')
    @Column(name = "admitido", length = 2)
    private String admitted;

    @Enumerated(EnumType.STRING)
    @Column(name = "categoria", length = 10)
    private ClientCategory category;

}
