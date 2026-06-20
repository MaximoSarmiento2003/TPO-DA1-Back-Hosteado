package com.example.tpoDA.dtos.auctioneer;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AuctioneerCreateDTO {

    private Integer personId;
    private String license;
    private String region;
}
