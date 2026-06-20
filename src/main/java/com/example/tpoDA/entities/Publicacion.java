package com.example.tpoDA.entities;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "publicaciones")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Publicacion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "titulo", nullable = false, length = 200)
    private String titulo;

    @Column(name = "categoria", length = 50)
    private String categoria;

    @Column(name = "precio_base_sugerido", precision = 18, scale = 2)
    private BigDecimal precioBaseSugerido;

    @Column(name = "autor_marca", length = 150)
    private String autorMarca;

    @Column(name = "anio")
    private Integer anio;

    @Column(name = "descripcion", length = 500)
    private String descripcion;

    @Column(name = "declara_propiedad")
    private Boolean declaraPropiedad;

    @Enumerated(EnumType.STRING)
    @Column(name = "estado", length = 25)
    private PublicacionEstado estado;

    @Column(name = "fecha_creacion")
    private LocalDateTime fechaCreacion;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cliente_id", nullable = false)
    private Client client;
}
