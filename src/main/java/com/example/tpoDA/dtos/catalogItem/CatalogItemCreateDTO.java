package com.example.tpoDA.dtos.catalogItem;

import lombok.*;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CatalogItemCreateDTO {

    private Integer catalogId;
    private Integer productId;

    private BigDecimal basePrice;
    private BigDecimal commission;

    private Boolean auctioned;
}
