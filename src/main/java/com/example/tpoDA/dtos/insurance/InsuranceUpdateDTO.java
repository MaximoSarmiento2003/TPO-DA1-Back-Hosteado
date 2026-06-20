package com.example.tpoDA.dtos.insurance;

import lombok.*;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class InsuranceUpdateDTO {

    private String company;
    private Boolean combinedPolicy;
    private BigDecimal amount;
}
