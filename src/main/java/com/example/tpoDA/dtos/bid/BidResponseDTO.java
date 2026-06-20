package com.example.tpoDA.dtos.bid;

import lombok.*;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BidResponseDTO {

    private Integer id;

    private Integer attendeeId;
    private Integer itemId;

    private BigDecimal amount;

    private Boolean winner;
}
