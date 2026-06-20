package com.example.tpoDA.dtos.product;

import lombok.*;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductBaseDTO {

    private LocalDate date;
    private Boolean available;

    private String catalogDescription;
    private String fullDescription;

    private Integer reviewerId;
    private Integer ownerId;
    private String insuranceId;
}
