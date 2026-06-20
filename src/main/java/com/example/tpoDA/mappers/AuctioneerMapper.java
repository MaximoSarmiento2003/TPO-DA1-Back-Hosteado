package com.example.tpoDA.mappers;

import com.example.tpoDA.dtos.auctioneer.AuctioneerCreateDTO;
import com.example.tpoDA.dtos.auctioneer.AuctioneerResponseDTO;
import com.example.tpoDA.dtos.auctioneer.AuctioneerUpdateDTO;
import com.example.tpoDA.entities.Auctioneer;
import com.example.tpoDA.entities.Person;

public class AuctioneerMapper {

    public static Auctioneer toEntity(AuctioneerCreateDTO dto, Person person) {
        if (dto == null || person == null) return null;

        return Auctioneer.builder()
                .person(person)
                .license(dto.getLicense())
                .region(dto.getRegion())
                .build();
    }

    public static AuctioneerResponseDTO toDTO(Auctioneer entity) {
        if (entity == null) return null;

        AuctioneerResponseDTO dto = new AuctioneerResponseDTO();

        dto.setId(entity.getId());

        if (entity.getPerson() != null) {
            dto.setName(entity.getPerson().getName());
            dto.setDocument(entity.getPerson().getDocument());
        }

        dto.setLicense(entity.getLicense());
        dto.setRegion(entity.getRegion());

        return dto;
    }

    public static void updateEntity(Auctioneer entity, AuctioneerUpdateDTO dto) {
        if (entity == null || dto == null) return;

        if (dto.getLicense() != null) {
            entity.setLicense(dto.getLicense());
        }

        if (dto.getRegion() != null) {
            entity.setRegion(dto.getRegion());
        }
    }
}
