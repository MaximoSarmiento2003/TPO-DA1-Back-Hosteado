package com.example.tpoDA.mappers;

import com.example.tpoDA.dtos.sector.SectorCreateDTO;
import com.example.tpoDA.dtos.sector.SectorResponseDTO;
import com.example.tpoDA.dtos.sector.SectorUpdateDTO;
import com.example.tpoDA.entities.Employee;
import com.example.tpoDA.entities.Sector;

public class SectorMapper {

    public static Sector toEntity(SectorCreateDTO dto, Employee employee) {
        if (dto == null) return null;

        return Sector.builder()
                .name(dto.getName())
                .code(dto.getCode())
                .responsible(employee)
                .build();
    }

    public static SectorResponseDTO toDTO(Sector entity) {
        if (entity == null) return null;

        SectorResponseDTO dto = new SectorResponseDTO();

        dto.setId(entity.getId());
        dto.setName(entity.getName());
        dto.setCode(entity.getCode());

        if (entity.getResponsible() != null) {
            dto.setResponsibleId(entity.getResponsible().getId());
            dto.setResponsiblePosition(entity.getResponsible().getPosition());
        }

        return dto;
    }

    public static void updateEntity(Sector entity, SectorUpdateDTO dto, Employee employee) {
        if (entity == null || dto == null) return;

        if (dto.getName() != null) {
            entity.setName(dto.getName());
        }

        if (dto.getCode() != null) {
            entity.setCode(dto.getCode());
        }

        if (employee != null) {
            entity.setResponsible(employee);
        }
    }
}
