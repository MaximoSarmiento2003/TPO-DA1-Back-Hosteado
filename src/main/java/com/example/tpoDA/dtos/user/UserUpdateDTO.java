package com.example.tpoDA.dtos.user;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserUpdateDTO {

    private String email;
    private String password;
}
