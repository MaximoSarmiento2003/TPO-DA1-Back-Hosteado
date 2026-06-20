package com.example.tpoDA.mappers;

import com.example.tpoDA.dtos.catalog.CatalogCreateDTO;
import com.example.tpoDA.dtos.catalog.CatalogResponseDTO;
import com.example.tpoDA.dtos.catalog.CatalogUpdateDTO;
import com.example.tpoDA.entities.Auction;
import com.example.tpoDA.entities.Catalog;
import com.example.tpoDA.entities.Employee;

public class CatalogMapper {

    public static Catalog toEntity(
            CatalogCreateDTO dto,
            Auction auction,
            Employee responsible
    ) {
        if (dto == null || responsible == null) return null;

        return Catalog.builder()
                .description(dto.getDescription())
                .auction(auction)
                .responsible(responsible)
                .build();
    }

    public static CatalogResponseDTO toDTO(Catalog entity) {
        if (entity == null) return null;

        CatalogResponseDTO dto = new CatalogResponseDTO();

        dto.setId(entity.getId());
        dto.setDescription(entity.getDescription());

        if (entity.getAuction() != null) {
            dto.setAuctionId(entity.getAuction().getId());
        }

        if (entity.getResponsible() != null) {
            dto.setResponsibleId(entity.getResponsible().getId());
        }

        return dto;
    }

    public static void updateEntity(
            Catalog entity,
            CatalogUpdateDTO dto,
            Auction auction,
            Employee responsible
    ) {
        if (entity == null || dto == null) return;

        if (dto.getDescription() != null) {
            entity.setDescription(dto.getDescription());
        }

        if (auction != null) {
            entity.setAuction(auction);
        }

        if (responsible != null) {
            entity.setResponsible(responsible);
        }
    }
}
