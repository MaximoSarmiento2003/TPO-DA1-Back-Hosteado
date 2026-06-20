package com.example.tpoDA.dtos.client;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ClientCreateDTO {

    private Integer personId;
    private Integer countryId;
    private Integer verifierId;

    private Boolean admitted;
    private String category;
}
