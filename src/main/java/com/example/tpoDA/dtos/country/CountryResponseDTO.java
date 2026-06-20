package com.example.tpoDA.dtos.country;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CountryResponseDTO {
    private Integer id;
    private String name;
    private String shortName;
    private String capital;
    private String nationality;
    private String languages;
}

