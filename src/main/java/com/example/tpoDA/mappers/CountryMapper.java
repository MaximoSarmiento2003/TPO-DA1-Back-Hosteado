package com.example.tpoDA.mappers;

import com.example.tpoDA.dtos.country.CountryCreateDTO;
import com.example.tpoDA.dtos.country.CountryResponseDTO;
import com.example.tpoDA.dtos.country.CountryUpdateDTO;
import com.example.tpoDA.entities.Country;

public class CountryMapper {

    public static Country toEntity(CountryCreateDTO dto) {
        if (dto == null) return null;

        return Country.builder()
                .id(dto.getId())
                .name(dto.getName())
                .shortName(dto.getShortName())
                .capital(dto.getCapital())
                .nationality(dto.getNationality())
                .languages(dto.getLanguages())
                .build();
    }

    public static CountryResponseDTO toDTO(Country entity) {
        if (entity == null) return null;

        return new CountryResponseDTO(
                entity.getId(),
                entity.getName(),
                entity.getShortName(),
                entity.getCapital(),
                entity.getNationality(),
                entity.getLanguages()
        );
    }

    public static void updateEntity(Country entity, CountryUpdateDTO dto) {
        if (entity == null || dto == null) return;

        entity.setName(dto.getName());
        entity.setShortName(dto.getShortName());
        entity.setCapital(dto.getCapital());
        entity.setNationality(dto.getNationality());
        entity.setLanguages(dto.getLanguages());
    }
}
