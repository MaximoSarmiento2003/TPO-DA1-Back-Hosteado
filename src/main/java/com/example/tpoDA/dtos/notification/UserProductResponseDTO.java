package com.example.tpoDA.dtos.notification;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserProductResponseDTO {
    // "ACCEPTED" | "REJECTED_BY_USER"
    private String response;
}
