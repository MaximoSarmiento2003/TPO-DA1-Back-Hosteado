package com.example.tpoDA.entities;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "fotos")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Photo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "identificador")
    private Integer id;

    //Producto
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "producto", nullable = false)
    private Product product;

    //Imagen
    @Lob
    @Column(name = "foto", nullable = false,columnDefinition = "LONGBLOB")
    private byte[] image;
}
