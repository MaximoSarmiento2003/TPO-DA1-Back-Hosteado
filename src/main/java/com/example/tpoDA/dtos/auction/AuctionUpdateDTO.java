package com.example.tpoDA.dtos.auction;

import lombok.*;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AuctionUpdateDTO {

    private LocalDate date;
    private LocalTime time;

    private String status;

    private Integer auctioneerId;

    private String location;
    private Integer capacity;

    private Boolean hasWarehouse;
    private Boolean privateSecurity;

    private String category;
}
