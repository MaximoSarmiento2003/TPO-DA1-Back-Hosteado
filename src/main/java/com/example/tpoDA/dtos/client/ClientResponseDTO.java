package com.example.tpoDA.dtos.client;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ClientResponseDTO {

    private Integer id;

    private String name;
    private String document;

    private String countryName;

    private Integer verifierId;

    private Boolean admitted;
    private String category;
}
