package com.example.tpoDA.dtos.catalogItem;

import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CatalogItemDetailDTO {

    // Ítem
    private Integer id;
    private String auctioned;
    private BigDecimal basePrice;    // null si visitante
    private BigDecimal commission;   // null si visitante

    // Producto
    private Integer productId;
    private String productType;      // "normal" | "art"
    private String productName;
    private String productBrand;
    private String catalogDescription;
    private String fullDescription;
    private String history;          // solo art
    private Integer quantity;
    private LocalDate productDate;

    // Subasta
    private Integer auctionId;
    private String auctionLocation;
    private String auctionStatus;
    private String auctionDate;
    private String auctionTime;

    // Rematador
    private String auctioneerName;
}
