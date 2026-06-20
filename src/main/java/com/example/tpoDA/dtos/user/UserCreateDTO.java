package com.example.tpoDA.dtos.user;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserCreateDTO {

    private Integer clientId;
    private String email;
    private String password;
}
