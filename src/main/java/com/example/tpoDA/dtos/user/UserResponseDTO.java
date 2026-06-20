package com.example.tpoDA.dtos.user;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserResponseDTO {

    private Integer id;
    private String email;

    // datos del cliente
    private String category;
    private Boolean admitted;

    // datos de persona
    private String name;
    private String document;
}