package com.example.tpoDA.entities;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "duenios")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Owner {

    @Id
    @Column(name = "identificador")
    private Integer id;

    //Person (PK compartida)
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
    @Column(name = "verificaciónFinanciera", length = 2)
    private String financialVerification;

    @Column(name = "verificaciónJudicial", length = 2)
    private String judicialVerification;

    // CHECK (1..6)
    @Column(name = "calificacionRiesgo")
    private Integer riskRating;
}
