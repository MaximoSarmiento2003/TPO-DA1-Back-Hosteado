package com.example.tpoDA.dtos.register;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SetPasswordRequestDTO {
    private String token;
    private String password;
}
