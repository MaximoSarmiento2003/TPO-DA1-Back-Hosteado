package com.example.tpoDA.dtos.product;

import lombok.*;
import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductApproveDTO {
    private BigDecimal basePrice;       // precio base propuesto
    private BigDecimal commissionRate;  // porcentaje de comisión, ej: 10.00
}
