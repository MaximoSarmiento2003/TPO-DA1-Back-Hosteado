package com.example.tpoDA.dtos.publicacion;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PublicacionResponseDTO {

    private Integer id;
    private String titulo;
    private String categoria;
    private BigDecimal precioBaseSugerido;
    private String autorMarca;
    private Integer anio;
    private String descripcion;
    private Boolean declaraPropiedad;
    private String estado;
    private LocalDateTime fechaCreacion;
}
