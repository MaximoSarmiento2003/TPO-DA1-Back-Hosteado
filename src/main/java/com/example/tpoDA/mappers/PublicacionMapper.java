package com.example.tpoDA.mappers;

import com.example.tpoDA.dtos.publicacion.PublicacionCreateDTO;
import com.example.tpoDA.dtos.publicacion.PublicacionResponseDTO;
import com.example.tpoDA.entities.Client;
import com.example.tpoDA.entities.Publicacion;
import com.example.tpoDA.entities.PublicacionEstado;

import java.time.LocalDateTime;

public class PublicacionMapper {

    public static Publicacion toEntity(PublicacionCreateDTO dto, Client client) {
        if (dto == null || client == null) return null;

        return Publicacion.builder()
                .titulo(dto.getTitulo())
                .categoria(dto.getCategoria())
                .precioBaseSugerido(dto.getPrecioBaseSugerido())
                .autorMarca(dto.getAutorMarca())
                .anio(dto.getAnio())
                .descripcion(dto.getDescripcion())
                .declaraPropiedad(dto.getDeclaraPropiedad())
                .estado(PublicacionEstado.PENDIENTE_REVISION)
                .fechaCreacion(LocalDateTime.now())
                .client(client)
                .build();
    }

    public static PublicacionResponseDTO toDTO(Publicacion entity) {
        if (entity == null) return null;

        PublicacionResponseDTO dto = new PublicacionResponseDTO();
        dto.setId(entity.getId());
        dto.setTitulo(entity.getTitulo());
        dto.setCategoria(entity.getCategoria());
        dto.setPrecioBaseSugerido(entity.getPrecioBaseSugerido());
        dto.setAutorMarca(entity.getAutorMarca());
        dto.setAnio(entity.getAnio());
        dto.setDescripcion(entity.getDescripcion());
        dto.setDeclaraPropiedad(entity.getDeclaraPropiedad());
        dto.setEstado(entity.getEstado() != null ? entity.getEstado().name() : null);
        dto.setFechaCreacion(entity.getFechaCreacion());
        return dto;
    }
}