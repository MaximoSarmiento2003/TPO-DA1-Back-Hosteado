package com.example.tpoDA.mappers;

import com.example.tpoDA.dtos.person.PersonCreateDTO;
import com.example.tpoDA.dtos.person.PersonResponseDTO;
import com.example.tpoDA.dtos.person.PersonUpdateDTO;
import com.example.tpoDA.entities.Person;
import com.example.tpoDA.entities.PersonStatus;

public class PersonMapper {

    public static Person toEntity(PersonCreateDTO dto) {
        if (dto == null) return null;

        return Person.builder()
                .document(dto.getDocument())
                .name(dto.getName())
                .address(dto.getAddress())
                .status(mapToEnum(dto.getStatus()))
                .photo(dto.getPhoto())
                .build();
    }

    public static PersonResponseDTO toDTO(Person entity) {
        if (entity == null) return null;

        return new PersonResponseDTO(
                entity.getId(),
                entity.getDocument(),
                entity.getName(),
                entity.getAddress(),
                mapToString(entity.getStatus())
        );
    }

    public static void updateEntity(Person entity, PersonUpdateDTO dto) {
        if (entity == null || dto == null) return;

        entity.setName(dto.getName());
        entity.setAddress(dto.getAddress());
        entity.setStatus(mapToEnum(dto.getStatus()));
        entity.setPhoto(dto.getPhoto());
    }

    // 🔽 Helpers para Enum

    private static PersonStatus mapToEnum(String status) {
        if (status == null) return null;

        return switch (status.toLowerCase()) {
            case "activo" -> PersonStatus.ACTIVO;
            case "incativo", "inactivo" -> PersonStatus.INACTIVO;
            default -> throw new IllegalArgumentException("Estado inválido: " + status);
        };
    }

    private static String mapToString(PersonStatus status) {
        if (status == null) return null;

        return switch (status) {
            case ACTIVO -> "activo";
            case INACTIVO -> "inactivo";
        };
    }
}
