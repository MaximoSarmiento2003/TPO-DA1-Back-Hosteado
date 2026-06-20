package com.example.tpoDA.dtos.product;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductUpdateDTO {

    private Boolean available;

    private String catalogDescription;
    private String fullDescription;

    private Integer reviewerId;
    private Integer ownerId;

    private String insuranceId;
}
