package com.example.tpoDA.dtos.auctioneer;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AuctioneerResponseDTO {

    private Integer id;

    private String name;
    private String document;

    private String license;
    private String region;
}
