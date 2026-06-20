package com.example.tpoDA.dtos.login;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AuthResponseDTO {

    private String token;

    private Integer userId;
    private Integer clientId;
    private String email;
    private String name;
    private String category;
    private Boolean admitted;
}
