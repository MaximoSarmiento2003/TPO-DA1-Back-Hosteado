package com.example.tpoDA.mappers;

import com.example.tpoDA.dtos.auction.AuctionCreateDTO;
import com.example.tpoDA.dtos.auction.AuctionResponseDTO;
import com.example.tpoDA.dtos.auction.AuctionUpdateDTO;
import com.example.tpoDA.dtos.catalogItem.CatalogItemResponseDTO;
import com.example.tpoDA.entities.*;

import java.util.List;

public class AuctionMapper {

    public static Auction toEntity(AuctionCreateDTO dto, Auctioneer auctioneer) {
        if (dto == null) return null;

        return Auction.builder()
                .date(dto.getDate())
                .time(dto.getTime())
                .status(toStatus(dto.getStatus()))
                .auctioneer(auctioneer)
                .location(dto.getLocation())
                .capacity(dto.getCapacity())
                .hasWarehouse(toDB(dto.getHasWarehouse()))
                .privateSecurity(toDB(dto.getPrivateSecurity()))
                .category(toCategory(dto.getCategory()))
                .build();
    }

    public static AuctionResponseDTO toDTO(Auction entity) {
        if (entity == null) return null;

        AuctionResponseDTO dto = new AuctionResponseDTO();
        dto.setId(entity.getId());
        dto.setDate(entity.getDate());
        dto.setTime(entity.getTime());
        dto.setStatus(fromStatus(entity.getStatus()));

        if (entity.getAuctioneer() != null) {
            dto.setAuctioneerId(entity.getAuctioneer().getId());
            if (entity.getAuctioneer().getPerson() != null) {
                dto.setAuctioneerName(entity.getAuctioneer().getPerson().getName());
            }
        }

        dto.setLocation(entity.getLocation());
        dto.setCapacity(entity.getCapacity());
        dto.setHasWarehouse(toBoolean(entity.getHasWarehouse()));
        dto.setPrivateSecurity(toBoolean(entity.getPrivateSecurity()));
        dto.setCategory(fromCategory(entity.getCategory()));

        int totalItems = 0;
        if (entity.getCatalogs() != null) {
            for (var catalog : entity.getCatalogs()) {
                if (catalog.getItems() != null) {
                    totalItems += catalog.getItems().size();
                }
            }
        }
        dto.setTotalItems(totalItems);

        return dto;
    }

    public static void updateEntity(Auction entity, AuctionUpdateDTO dto, Auctioneer auctioneer) {
        if (entity == null || dto == null) return;

        if (dto.getDate() != null) entity.setDate(dto.getDate());
        if (dto.getTime() != null) entity.setTime(dto.getTime());
        if (dto.getStatus() != null) entity.setStatus(toStatus(dto.getStatus()));
        if (auctioneer != null) entity.setAuctioneer(auctioneer);
        if (dto.getLocation() != null) entity.setLocation(dto.getLocation());
        if (dto.getCapacity() != null) entity.setCapacity(dto.getCapacity());
        if (dto.getHasWarehouse() != null) entity.setHasWarehouse(toDB(dto.getHasWarehouse()));
        if (dto.getPrivateSecurity() != null) entity.setPrivateSecurity(toDB(dto.getPrivateSecurity()));
        if (dto.getCategory() != null) entity.setCategory(toCategory(dto.getCategory()));
    }

    // ─── Catálogo ─────────────────────────────────────────────────────────────

    // showPrice=false cuando el request no tiene JWT válido
    public static CatalogItemResponseDTO toCatalogItemDTO(CatalogItem item, boolean showPrice) {
        if (item == null) return null;

        Product product = item.getProduct();
        String name = "";
        String brand = null;
        String type = "normal";

        if (product instanceof NormalProduct np) {
            name  = np.getName();
            brand = np.getBrand();
            type  = "normal";
        } else if (product instanceof ArtProduct ap) {
            name  = ap.getName();
            brand = ap.getBrand();
            type  = "art";
        }

        return CatalogItemResponseDTO.builder()
                .id(item.getId())
                .productName(name)
                .productBrand(brand)
                .productDescription(product != null ? product.getCatalogDescription() : null)
                .productType(type)
                .auctioned(item.getAuctioned())
                .basePrice(showPrice ? item.getBasePrice() : null)
                .commission(showPrice ? item.getCommission() : null)
                .build();
    }

    public static List<CatalogItemResponseDTO> toCatalogItemDTOList(
            List<CatalogItem> items, boolean showPrice) {
        if (items == null) return List.of();
        return items.stream()
                .map(i -> toCatalogItemDTO(i, showPrice))
                .toList();
    }

    // ─── helpers ─────────────────────────────────────────────────────────────

    private static String toDB(Boolean v) {
        if (v == null) return null;
        return v ? "si" : "no";
    }

    private static Boolean toBoolean(String v) {
        if (v == null) return null;
        return v.equalsIgnoreCase("si");
    }

    private static AuctionStatus toStatus(String v) {
        if (v == null) return null;
        return switch (v.toLowerCase()) {
            case "abierta" -> AuctionStatus.ABIERTA;
            case "cerrada" -> AuctionStatus.CERRADA;
            default -> throw new IllegalArgumentException("Estado inválido: " + v);
        };
    }

    private static String fromStatus(AuctionStatus s) {
        if (s == null) return null;
        return switch (s) {
            case ABIERTA -> "abierta";
            case CERRADA -> "cerrada";
        };
    }

    private static ClientCategory toCategory(String v) {
        if (v == null) return null;
        return ClientCategory.valueOf(v.toUpperCase());
    }

    private static String fromCategory(ClientCategory c) {
        if (c == null) return null;
        return c.name().toLowerCase();
    }
}
