package com.example.tpoDA.mappers;

import java.util.Base64;

import com.example.tpoDA.dtos.photo.PhotoCreateDTO;
import com.example.tpoDA.dtos.photo.PhotoResponseDTO;
import com.example.tpoDA.entities.Photo;
import com.example.tpoDA.entities.Product;

public class PhotoMapper {

    public static Photo toEntity(PhotoCreateDTO dto, Product product) {
        if (dto == null || product == null) return null;

        return Photo.builder()
                .product(product)
                .image(Base64.getDecoder().decode(dto.getImageBase64()))
                .build();
    }

    public static PhotoResponseDTO toDTO(Photo entity) {
        if (entity == null) return null;

        return new PhotoResponseDTO(
                entity.getId(),
                entity.getProduct().getId(),
                Base64.getEncoder().encodeToString(entity.getImage())
        );
    }
}
