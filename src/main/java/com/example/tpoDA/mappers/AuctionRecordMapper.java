package com.example.tpoDA.mappers;

import com.example.tpoDA.dtos.auctionRecord.AuctionRecordCreateDTO;
import com.example.tpoDA.dtos.auctionRecord.AuctionRecordResponseDTO;
import com.example.tpoDA.entities.Auction;
import com.example.tpoDA.entities.AuctionRecord;
import com.example.tpoDA.entities.Client;
import com.example.tpoDA.entities.Owner;
import com.example.tpoDA.entities.Product;

public class AuctionRecordMapper {

    public static AuctionRecord toEntity(
            AuctionRecordCreateDTO dto,
            Auction auction,
            Owner owner,
            Product product,
            Client client
    ) {
        if (dto == null || auction == null || owner == null 
            || product == null || client == null) return null;

        return AuctionRecord.builder()
                .auction(auction)
                .owner(owner)
                .product(product)
                .client(client)
                .amount(dto.getAmount())
                .commission(dto.getCommission())
                .build();
    }

    public static AuctionRecordResponseDTO toDTO(AuctionRecord entity) {
        if (entity == null) return null;

        AuctionRecordResponseDTO dto = new AuctionRecordResponseDTO();

        dto.setId(entity.getId());

        if (entity.getAuction() != null) {
            dto.setAuctionId(entity.getAuction().getId());
        }

        if (entity.getOwner() != null) {
            dto.setOwnerId(entity.getOwner().getId());
        }

        if (entity.getProduct() != null) {
            dto.setProductId(entity.getProduct().getId());
        }

        if (entity.getClient() != null) {
            dto.setClientId(entity.getClient().getId());
        }

        dto.setAmount(entity.getAmount());
        dto.setCommission(entity.getCommission());

        return dto;
    }
}
