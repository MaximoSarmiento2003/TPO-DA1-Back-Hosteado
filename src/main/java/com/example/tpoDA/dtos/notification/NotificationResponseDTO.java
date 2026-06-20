package com.example.tpoDA.dtos.notification;

import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NotificationResponseDTO {
    private Integer id;
    private String type;
    private String message;
    private BigDecimal basePrice;
    private BigDecimal commissionRate;
    private BigDecimal amount;
    private Integer minutesToStart;
    private Integer productId;
    private String productName;
    private Integer auctionId;
    private String auctionLocation;
    private LocalDateTime createdAt;
    private boolean read;
    private String userResponse;
}
