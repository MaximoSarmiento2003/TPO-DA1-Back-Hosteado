package com.example.tpoDA.dtos.login;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginRequestDTO {

    private String email;

    private String password;
}
