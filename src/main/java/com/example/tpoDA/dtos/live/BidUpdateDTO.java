package com.example.tpoDA.dtos.live;

import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class BidUpdateDTO {
    private Integer  bidId;
    private Integer  itemId;
    private Integer  auctionId;
    private BigDecimal amount;
    private Integer  bidderNumber;
    private LocalDateTime timestamp;
    private BigDecimal minNextBid;   // mínimo para la próxima oferta
    private BigDecimal maxNextBid;   // máximo (null si ORO/PLATINO)
}
