package com.example.tpoDA.dtos.person;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PersonCreateDTO {

    private String document;
    private String name;
    private String address;
    private String status;
    private byte[] photo;
}
