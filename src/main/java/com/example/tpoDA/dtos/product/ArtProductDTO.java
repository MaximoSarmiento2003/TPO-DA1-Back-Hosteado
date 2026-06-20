package com.example.tpoDA.dtos.product;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class ArtProductDTO extends ProductBaseDTO {

    private String name;
    private String brand;
    private Integer quantity;
    private Boolean ownerConfirmed;

    private String history;
}
