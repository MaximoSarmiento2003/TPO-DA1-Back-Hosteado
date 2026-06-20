package com.example.tpoDA.dtos.live;

import lombok.*;
import java.math.BigDecimal;

@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class JoinAuctionResponseDTO {
    private Integer auctionId;
    private Integer currentItemId;
    private String  currentItemName;
    private String  currentItemDescription;
    private BigDecimal basePrice;
    private BigDecimal currentBid;       // mejor oferta actual (null si no hay)
    private Integer bidderNumber;        // número de postor asignado
    private boolean canBid;             // tiene medio de pago verificado y sin multas
    private String  streamingLink;       // link del streaming
    private String  currency;            // "ARS" | "USD"
}
