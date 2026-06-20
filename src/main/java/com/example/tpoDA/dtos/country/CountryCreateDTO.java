package com.example.tpoDA.dtos.country;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CountryCreateDTO {

    private Integer id;
    private String name;
    private String shortName;
    private String capital;
    private String nationality;
    private String languages;
}

