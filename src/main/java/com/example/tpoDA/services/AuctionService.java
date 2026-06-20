package com.example.tpoDA.services;

import com.example.tpoDA.dtos.auction.AuctionResponseDTO;
import com.example.tpoDA.dtos.catalogItem.CatalogItemDetailDTO;
import com.example.tpoDA.dtos.catalogItem.CatalogItemResponseDTO;
import com.example.tpoDA.entities.*;
import com.example.tpoDA.exceptions.AppException;
import com.example.tpoDA.mappers.AuctionMapper;
import com.example.tpoDA.repositories.AuctionRepository;
import com.example.tpoDA.repositories.CatalogItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuctionService {

    private final AuctionRepository auctionRepository;
    private final CatalogItemRepository catalogItemRepository;

    public List<AuctionResponseDTO> getAllAuctions() {
        return auctionRepository.findAllByOrderByDateDescTimeDesc()
                .stream().map(AuctionMapper::toDTO).toList();
    }

    public AuctionResponseDTO getById(Integer id) {
        return auctionRepository.findById(id)
                .map(AuctionMapper::toDTO)
                .orElseThrow(() -> new AppException("Subasta no encontrada", HttpStatus.NOT_FOUND));
    }

    public List<CatalogItemResponseDTO> getCatalog(Integer auctionId, boolean showPrice) {
        if (!auctionRepository.existsById(auctionId))
            throw new AppException("Subasta no encontrada", HttpStatus.NOT_FOUND);
        return AuctionMapper.toCatalogItemDTOList(
                catalogItemRepository.findByAuctionId(auctionId), showPrice);
    }

    public CatalogItemDetailDTO getItemDetail(Integer itemId, boolean showPrice) {
        CatalogItem item = catalogItemRepository.findDetailById(itemId)
                .orElseThrow(() -> new AppException("Ítem no encontrado", HttpStatus.NOT_FOUND));

        Product product = item.getProduct();
        String name = "", brand = null, type = "normal", history = null;
        Integer quantity = null;

        if (product instanceof NormalProduct np) {
            name = np.getName(); brand = np.getBrand(); quantity = np.getQuantity();
        } else if (product instanceof ArtProduct ap) {
            name = ap.getName(); brand = ap.getBrand();
            type = "art"; history = ap.getHistory(); quantity = ap.getQuantity();
        }

        Auction auction = item.getCatalog().getAuction();
        String auctioneerName = null;
        if (auction.getAuctioneer() != null && auction.getAuctioneer().getPerson() != null)
            auctioneerName = auction.getAuctioneer().getPerson().getName();

        return CatalogItemDetailDTO.builder()
                .id(item.getId())
                .auctioned(item.getAuctioned())
                .basePrice(showPrice ? item.getBasePrice() : null)
                .commission(showPrice ? item.getCommission() : null)
                .productId(product.getId())
                .productType(type)
                .productName(name)
                .productBrand(brand)
                .catalogDescription(product.getCatalogDescription())
                .fullDescription(product.getFullDescription())
                .history(history)
                .quantity(quantity)
                .productDate(product.getDate())
                .auctionId(auction.getId())
                .auctionLocation(auction.getLocation())
                .auctionStatus(auction.getStatus() != null ? auction.getStatus().name().toLowerCase() : null)
                .auctionDate(auction.getDate() != null ? auction.getDate().toString() : null)
                .auctionTime(auction.getTime() != null ? auction.getTime().toString() : null)
                .auctioneerName(auctioneerName)
                .build();
    }

    // Para resolver el productId desde un itemId (usado en el endpoint de foto)
    public Optional<Integer> getProductIdFromItem(Integer itemId) {
        return catalogItemRepository.findById(itemId)
                .map(ci -> ci.getProduct().getId());
    }
}
