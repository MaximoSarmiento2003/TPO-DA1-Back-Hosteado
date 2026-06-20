package com.example.tpoDA.dtos.bid;

import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BidHistoryResponseDTO {
    private Integer bidId;

    // Producto
    private String productName;
    private String productBrand;
    private String productType;    // "normal" | "art"

    // Subasta
    private Integer auctionId;
    private LocalDate auctionDate;
    private String auctionLocation;

    // Puja
    private BigDecimal amount;
    private Boolean winner;        // true = ganó, false = perdió, null = en curso
}
