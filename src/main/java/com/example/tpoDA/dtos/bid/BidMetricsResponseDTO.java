package com.example.tpoDA.dtos.bid;

import lombok.*;
import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BidMetricsResponseDTO {
    private int totalBids;          // pujas totales (individuales)
    private int auctionsAttended;   // subastas en las que participó
    private int auctionsWon;        // subastas que ganó (al menos un ítem)
    private int auctionsLost;       // subastas en las que participó pero no ganó ningún ítem
    private BigDecimal totalSpent;  // suma de importes de pujas ganadas
    private BigDecimal totalBid;    // suma de todos los importes ofertados
}
