package com.example.tpoDA.mappers;

import com.example.tpoDA.dtos.employee.EmployeeCreateDTO;
import com.example.tpoDA.dtos.employee.EmployeeResponseDTO;
import com.example.tpoDA.dtos.employee.EmployeeUpdateDTO;
import com.example.tpoDA.entities.Employee;

public class EmployeeMapper {

    public static Employee toEntity(EmployeeCreateDTO dto) {
        if (dto == null) return null;

        return Employee.builder()
                .id(dto.getId())
                .position(dto.getPosition())
                .sector(dto.getSector())
                .build();
    }

    public static EmployeeResponseDTO toDTO(Employee entity) {
        if (entity == null) return null;

        return new EmployeeResponseDTO(
                entity.getId(),
                entity.getPosition(),
                entity.getSector()
        );
    }

    public static void updateEntity(Employee entity, EmployeeUpdateDTO dto) {
        if (entity == null || dto == null) return;

        if (dto.getPosition() != null) {
            entity.setPosition(dto.getPosition());
        }

        if (dto.getSector() != null) {
            entity.setSector(dto.getSector());
        }
    }
}

