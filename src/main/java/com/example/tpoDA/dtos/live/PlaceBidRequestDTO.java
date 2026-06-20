package com.example.tpoDA.dtos.live;

import lombok.*;
import java.math.BigDecimal;

@Data @NoArgsConstructor @AllArgsConstructor
public class PlaceBidRequestDTO {
    private Integer auctionId;
    private Integer itemId;
    private BigDecimal amount;
}
