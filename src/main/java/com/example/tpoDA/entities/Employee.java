package com.example.tpoDA.entities;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "empleados")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Employee {

    @Id
    @Column(name = "identificador")
    private Integer id;

    @Column(name = "cargo", length = 100)
    private String position;

    @Column(name = "sector")
    private Integer sector;
}
