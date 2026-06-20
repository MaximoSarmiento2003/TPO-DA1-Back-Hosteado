package com.example.tpoDA.dtos.attendee;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AttendeeResponseDTO {

    private Integer id;
    private Integer bidderNumber;

    private Integer clientId;
    private Integer auctionId;
}
