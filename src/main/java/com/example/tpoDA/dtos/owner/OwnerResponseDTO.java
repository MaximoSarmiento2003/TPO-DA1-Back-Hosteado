package com.example.tpoDA.dtos.owner;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OwnerResponseDTO {

    private Integer id;

    private String name;
    private String document;

    private String countryName;

    private Integer verifierId;

    private Boolean financialVerification;
    private Boolean judicialVerification;

    private Integer riskRating;
}
