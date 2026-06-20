package com.example.tpoDA.dtos.user;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChangePasswordDTO {

    private String passwordActual;
    private String passwordNueva;
    private String confirmPassword;
}

