package com.example.tpoDA.mappers;

import com.example.tpoDA.dtos.catalogItem.CatalogItemCreateDTO;
import com.example.tpoDA.dtos.catalogItem.CatalogItemResponseDTO;
import com.example.tpoDA.dtos.catalogItem.CatalogItemUpdateDTO;
import com.example.tpoDA.entities.Catalog;
import com.example.tpoDA.entities.CatalogItem;
import com.example.tpoDA.entities.Product;

public class CatalogItemMapper {

    public static CatalogItem toEntity(
            CatalogItemCreateDTO dto,
            Catalog catalog,
            Product product
    ) {
        if (dto == null || catalog == null || product == null) return null;

        return CatalogItem.builder()
                .catalog(catalog)
                .product(product)
                .basePrice(dto.getBasePrice())
                .commission(dto.getCommission())
                .auctioned(toDB(dto.getAuctioned()))
                .build();
    }

    public static CatalogItemResponseDTO toDTO(CatalogItem entity) {
    if (entity == null) return null;

    CatalogItemResponseDTO dto = new CatalogItemResponseDTO();

    dto.setId(entity.getId());

    if (entity.getCatalog() != null) {
        dto.setCatalogId(entity.getCatalog().getId());
    }

    if (entity.getProduct() != null) {
        dto.setProductId(entity.getProduct().getId());
    }

    dto.setBasePrice(entity.getBasePrice());
    dto.setCommission(entity.getCommission());

    dto.setAuctioned(entity.getAuctioned());

    return dto;
}

    public static void updateEntity(
            CatalogItem entity,
            CatalogItemUpdateDTO dto
    ) {
        if (entity == null || dto == null) return;

        if (dto.getBasePrice() != null) {
            entity.setBasePrice(dto.getBasePrice());
        }

        if (dto.getCommission() != null) {
            entity.setCommission(dto.getCommission());
        }

        if (dto.getAuctioned() != null) {
            entity.setAuctioned(toDB(dto.getAuctioned()));
        }
    }

    //helpers

    private static String toDB(Boolean value) {
        if (value == null) return null;
        return value ? "si" : "no";
    }

    private static Boolean toBoolean(String value) {
        if (value == null) return null;
        return value.equalsIgnoreCase("si");
    }
}
