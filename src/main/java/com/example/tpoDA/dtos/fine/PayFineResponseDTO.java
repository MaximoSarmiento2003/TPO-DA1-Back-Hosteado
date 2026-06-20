package com.example.tpoDA.dtos.fine;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PayFineResponseDTO {
    private String message;
    private boolean hasPendingFines;
}
