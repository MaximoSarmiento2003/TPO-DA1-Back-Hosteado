package com.example.tpoDA.dtos.catalogItem;

import lombok.*;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CatalogItemUpdateDTO {

    private BigDecimal basePrice;
    private BigDecimal commission;

    private Boolean auctioned;
}
