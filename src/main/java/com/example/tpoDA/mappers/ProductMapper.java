package com.example.tpoDA.mappers;

import java.time.LocalDate;

import com.example.tpoDA.dtos.product.ProductCreateDTO;
import com.example.tpoDA.dtos.product.ProductResponseDTO;
import com.example.tpoDA.dtos.product.ProductUpdateDTO;
import com.example.tpoDA.entities.Employee;
import com.example.tpoDA.entities.Insurance;
import com.example.tpoDA.entities.Owner;
import com.example.tpoDA.entities.Product;

public class ProductMapper {

    public static Product toEntity(
            ProductCreateDTO dto,
            Employee reviewer,
            Owner owner,
            Insurance insurance
    ) {

        if (dto == null || reviewer == null || owner == null) return null;

        return Product.builder()
                .date(LocalDate.now())
                .available("si")
                .catalogDescription(
                        dto.getCatalogDescription() != null
                                ? dto.getCatalogDescription()
                                : "No Posee"
                )
                .fullDescription(dto.getFullDescription())
                .reviewer(reviewer)
                .owner(owner)
                .insurance(insurance)
                .build();
    }

    public static ProductResponseDTO toDTO(Product entity) {

        if (entity == null) return null;

        ProductResponseDTO dto = new ProductResponseDTO();

        dto.setId(entity.getId());
        dto.setDate(entity.getDate());

        // DTO usa String
        dto.setAvailable(entity.getAvailable());

        dto.setCatalogDescription(entity.getCatalogDescription());
        dto.setFullDescription(entity.getFullDescription());

        return dto;
    }

    public static void updateEntity(
            Product entity,
            ProductUpdateDTO dto,
            Employee reviewer,
            Owner owner,
            Insurance insurance
    ) {

        if (entity == null || dto == null) return;

        if (dto.getAvailable() != null) {
            entity.setAvailable(toDB(dto.getAvailable()));
        }

        if (dto.getCatalogDescription() != null) {
            entity.setCatalogDescription(dto.getCatalogDescription());
        }

        if (dto.getFullDescription() != null) {
            entity.setFullDescription(dto.getFullDescription());
        }

        if (reviewer != null) {
            entity.setReviewer(reviewer);
        }

        if (owner != null) {
            entity.setOwner(owner);
        }

        if (insurance != null) {
            entity.setInsurance(insurance);
        }
    }

    private static String toDB(Boolean value) {
        if (value == null) return null;
        return value ? "si" : "no";
    }
}