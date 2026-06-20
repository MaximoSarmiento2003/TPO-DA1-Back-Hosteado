package com.example.tpoDA.mappers;

import com.example.tpoDA.dtos.attendee.AttendeeCreateDTO;
import com.example.tpoDA.dtos.attendee.AttendeeResponseDTO;
import com.example.tpoDA.dtos.attendee.AttendeeUpdateDTO;
import com.example.tpoDA.entities.Attendee;
import com.example.tpoDA.entities.Auction;
import com.example.tpoDA.entities.Client;

public class AttendeeMapper {

    public static Attendee toEntity(
            AttendeeCreateDTO dto,
            Client client,
            Auction auction
    ) {
        if (dto == null || client == null || auction == null) return null;

        return Attendee.builder()
                .bidderNumber(dto.getBidderNumber())
                .client(client)
                .auction(auction)
                .build();
    }

    public static AttendeeResponseDTO toDTO(Attendee entity) {
        if (entity == null) return null;

        AttendeeResponseDTO dto = new AttendeeResponseDTO();

        dto.setId(entity.getId());
        dto.setBidderNumber(entity.getBidderNumber());

        if (entity.getClient() != null) {
            dto.setClientId(entity.getClient().getId());
        }

        if (entity.getAuction() != null) {
            dto.setAuctionId(entity.getAuction().getId());
        }

        return dto;
    }

    public static void updateEntity(Attendee entity, AttendeeUpdateDTO dto) {
        if (entity == null || dto == null) return;

        if (dto.getBidderNumber() != null) {
            entity.setBidderNumber(dto.getBidderNumber());
        }
    }
}
