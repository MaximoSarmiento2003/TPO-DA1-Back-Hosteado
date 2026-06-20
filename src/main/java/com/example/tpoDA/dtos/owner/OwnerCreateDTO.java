package com.example.tpoDA.dtos.owner;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OwnerCreateDTO {

    private Integer personId;
    private Integer countryId;
    private Integer verifierId;

    private Boolean financialVerification;
    private Boolean judicialVerification;

    private Integer riskRating;
}
