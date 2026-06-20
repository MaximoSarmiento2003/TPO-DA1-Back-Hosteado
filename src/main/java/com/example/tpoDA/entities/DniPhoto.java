package com.example.tpoDA.entities;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "dni_photos")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DniPhoto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    // Referencia a la persona dueña de las fotos
    @Column(name = "person_id", nullable = false, unique = true)
    private Integer personId;

    @Lob
    @Column(name = "foto_frente",columnDefinition = "LONGBLOB")
    private byte[] frente;

    @Lob
    @Column(name = "foto_dorso",columnDefinition = "LONGBLOB")
    private byte[] dorso;
}
