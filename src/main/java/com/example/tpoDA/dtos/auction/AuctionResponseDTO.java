package com.example.tpoDA.dtos.auction;

import lombok.*;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AuctionResponseDTO {

    private Integer id;

    private LocalDate date;
    private LocalTime time;

    private String status;       // "abierta" | "cerrada"

    private Integer auctioneerId;
    private String auctioneerName;  // nombre del rematador para mostrar en la card

    private String location;
    private Integer capacity;

    private Boolean hasWarehouse;
    private Boolean privateSecurity;

    private String category;    // categoría mínima requerida

    private Integer totalItems; // cantidad de ítems en el catálogo
}
