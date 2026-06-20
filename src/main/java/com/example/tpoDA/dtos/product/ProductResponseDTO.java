package com.example.tpoDA.dtos.product;

import lombok.*;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductResponseDTO {
    private Integer id;
    private String type;
    private String name;
    private String brand;
    private Integer quantity;
    private String catalogDescription;
    private String fullDescription;
    private String history;
    private LocalDate date;
    private String available;
    private Integer photoCount;
}
