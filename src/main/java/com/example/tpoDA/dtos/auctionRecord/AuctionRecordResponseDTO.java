package com.example.tpoDA.dtos.auctionRecord;

import lombok.*;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AuctionRecordResponseDTO {

    private Integer id;

    private Integer auctionId;
    private Integer ownerId;
    private Integer productId;
    private Integer clientId;

    private BigDecimal amount;
    private BigDecimal commission;
}
