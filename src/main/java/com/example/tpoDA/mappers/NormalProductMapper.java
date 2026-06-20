package com.example.tpoDA.mappers;

import com.example.tpoDA.dtos.product.NormalProductDTO;
import com.example.tpoDA.entities.Employee;
import com.example.tpoDA.entities.Insurance;
import com.example.tpoDA.entities.NormalProduct;
import com.example.tpoDA.entities.Owner;

public class NormalProductMapper {

    public static NormalProduct toEntity(
            NormalProductDTO dto,
            Employee reviewer,
            Owner owner,
            Insurance insurance
    ) {
        if (dto == null || reviewer == null || owner == null) return null;

        return NormalProduct.builder()
                // 🔽 BASE
                .date(dto.getDate())
                .available(toDB(dto.getAvailable()))
                .catalogDescription(dto.getCatalogDescription() != null 
                        ? dto.getCatalogDescription() 
                        : "No Posee")
                .fullDescription(dto.getFullDescription())
                .reviewer(reviewer)
                .owner(owner)
                .insurance(insurance)

                // 🔽 ESPECÍFICO
                .name(dto.getName())
                .brand(dto.getBrand())
                .quantity(dto.getQuantity())
                .isOwnerConfirmed(toDB(dto.getOwnerConfirmed()))

                .build();
    }

    public static NormalProductDTO toDTO(NormalProduct entity) {
        if (entity == null) return null;

        NormalProductDTO dto = new NormalProductDTO();

        // 🔽 BASE
        dto.setDate(entity.getDate());
        dto.setAvailable(toBoolean(entity.getAvailable()));
        dto.setCatalogDescription(entity.getCatalogDescription());
        dto.setFullDescription(entity.getFullDescription());

        if (entity.getReviewer() != null) {
            dto.setReviewerId(entity.getReviewer().getId());
        }

        if (entity.getOwner() != null) {
            dto.setOwnerId(entity.getOwner().getId());
        }

        if (entity.getInsurance() != null) {
            dto.setInsuranceId(entity.getInsurance().getPolicyNumber());
        }

        // 🔽 ESPECÍFICO
        dto.setName(entity.getName());
        dto.setBrand(entity.getBrand());
        dto.setQuantity(entity.getQuantity());
        dto.setOwnerConfirmed(toBoolean(entity.getIsOwnerConfirmed()));

        return dto;
    }

    // 🔁 helpers

    private static String toDB(Boolean value) {
        if (value == null) return null;
        return value ? "si" : "no";
    }

    private static Boolean toBoolean(String value) {
        if (value == null) return null;
        return value.equalsIgnoreCase("si");
    }
}
