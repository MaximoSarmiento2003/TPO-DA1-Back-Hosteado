package com.example.tpoDA.dtos.admin;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RejectClientDTO {
    // Motivo del rechazo (opcional, se incluye en el mail)
    private String reason;
}
