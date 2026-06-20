package com.example.tpoDA.dtos.publicacion;

import lombok.*;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PublicacionCreateDTO {

    private String titulo;
    private String categoria;
    private BigDecimal precioBaseSugerido;
    private String autorMarca;
    private Integer anio;
    private String descripcion;
    private Boolean declaraPropiedad;
}

