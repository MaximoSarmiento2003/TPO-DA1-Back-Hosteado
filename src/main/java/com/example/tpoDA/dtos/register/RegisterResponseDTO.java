package com.example.tpoDA.dtos.register;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RegisterResponseDTO {
    private String message;
    private String email;
}
