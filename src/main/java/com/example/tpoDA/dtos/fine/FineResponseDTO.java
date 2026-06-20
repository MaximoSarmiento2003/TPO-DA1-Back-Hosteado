package com.example.tpoDA.dtos.fine;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FineResponseDTO {
    private Integer id;
    private BigDecimal amount;
    private LocalDate dueDate;
    private String status;
    private Integer bidId;
    private BigDecimal bidAmount;
    private String productName;
    private Integer auctionId;
}