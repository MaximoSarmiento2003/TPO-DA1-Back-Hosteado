package com.example.tpoDA.dtos.client;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ClientUpdateDTO {

    private Integer countryId;
    private Integer verifierId;

    private Boolean admitted;
    private String category;
}
