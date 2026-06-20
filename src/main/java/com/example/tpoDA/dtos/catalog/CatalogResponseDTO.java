package com.example.tpoDA.dtos.catalog;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CatalogResponseDTO {

    private Integer id;
    private String description;

    private Integer auctionId;
    private Integer responsibleId;
}
