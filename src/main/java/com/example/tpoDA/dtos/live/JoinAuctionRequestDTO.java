package com.example.tpoDA.dtos.live;

import lombok.*;

@Data @NoArgsConstructor @AllArgsConstructor
public class JoinAuctionRequestDTO {
    private Integer paymentMethodId;  // null si solo quiere ver
}
