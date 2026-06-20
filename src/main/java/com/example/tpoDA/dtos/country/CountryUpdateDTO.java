package com.example.tpoDA.dtos.country;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CountryUpdateDTO {

    private String name;
    private String shortName;
    private String capital;
    private String nationality;
    private String languages;
}
