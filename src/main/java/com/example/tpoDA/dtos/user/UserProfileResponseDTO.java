package com.example.tpoDA.dtos.user;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserProfileResponseDTO {

    private Integer id;
    private String nombre;
    private String email;
    private String domicilioLegal;
    private String paisOrigen;
    private String categoria;
    private String estado;
}
