package com.example.tpoDA.mappers;

import com.example.tpoDA.dtos.bid.BidCreateDTO;
import com.example.tpoDA.dtos.bid.BidResponseDTO;
import com.example.tpoDA.entities.Attendee;
import com.example.tpoDA.entities.Bid;
import com.example.tpoDA.entities.CatalogItem;

public class BidMapper {

    public static Bid toEntity(
            BidCreateDTO dto,
            Attendee attendee,
            CatalogItem item
    ) {
        if (dto == null || attendee == null || item == null) return null;

        return Bid.builder()
                .attendee(attendee)
                .item(item)
                .amount(dto.getAmount())
                .winner("no") // default
                .build();
    }

    public static BidResponseDTO toDTO(Bid entity) {
        if (entity == null) return null;

        BidResponseDTO dto = new BidResponseDTO();

        dto.setId(entity.getId());

        if (entity.getAttendee() != null) {
            dto.setAttendeeId(entity.getAttendee().getId());
        }

        if (entity.getItem() != null) {
            dto.setItemId(entity.getItem().getId());
        }

        dto.setAmount(entity.getAmount());
        dto.setWinner(toBoolean(entity.getWinner()));

        return dto;
    }

    private static Boolean toBoolean(String value) {
        if (value == null) return null;
        return value.equalsIgnoreCase("si");
    }
}
