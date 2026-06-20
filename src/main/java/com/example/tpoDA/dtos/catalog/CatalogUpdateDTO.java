package com.example.tpoDA.dtos.catalog;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CatalogUpdateDTO {

    private String description;
    private Integer auctionId;
    private Integer responsibleId;
}
