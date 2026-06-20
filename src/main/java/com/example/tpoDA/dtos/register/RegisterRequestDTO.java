package com.example.tpoDA.dtos.register;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RegisterRequestDTO {

    // Datos personales
    private String nombre;
    private String apellido;
    private String document;
    private String address;

    // Fotos del DNI en Base64
    private String dniFrenteBase64;
    private String dniDorsoBase64;

    // Opcional
    private Integer countryId;

    // Credencial — solo el email, la contraseña se setea después
    private String email;
}