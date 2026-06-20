package com.example.tpoDA.dtos.live;

import lombok.*;
import java.math.BigDecimal;

@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class ItemResultDTO {
    private Integer  itemId;
    private Integer  auctionId;
    private String   itemName;
    private boolean  sold;           // false = lo compra la empresa
    private Integer  winnerBidderNumber;
    private BigDecimal finalAmount;
    private BigDecimal commission;
    private BigDecimal shippingCost;
    // Siguiente ítem (null si fue el último)
    private Integer  nextItemId;
    private String   nextItemName;
    private BigDecimal nextBasePrice;
    private boolean  auctionFinished;
}
