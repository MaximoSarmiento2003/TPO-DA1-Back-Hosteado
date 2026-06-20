package com.example.tpoDA.dtos.product;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductRejectDTO {
    private String reason;  // motivo del rechazo (opcional)
}
