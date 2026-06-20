package com.example.tpoDA.dtos.catalogItem;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CatalogItemResponseDTO {

    private Integer id;
    private Integer catalogId;
    private Integer productId;

    private String productName;
    private String productBrand;
    private String productDescription;
    private String productType;

    private String auctioned;

    private BigDecimal basePrice;
    private BigDecimal commission;
}