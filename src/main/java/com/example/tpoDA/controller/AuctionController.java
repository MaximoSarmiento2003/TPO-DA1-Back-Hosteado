package com.example.tpoDA.controller;

import com.example.tpoDA.dtos.auction.AuctionResponseDTO;
import com.example.tpoDA.dtos.catalogItem.CatalogItemDetailDTO;
import com.example.tpoDA.dtos.catalogItem.CatalogItemResponseDTO;
import com.example.tpoDA.repositories.PhotoRepository;
import com.example.tpoDA.services.AuctionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/auctions")
@RequiredArgsConstructor
public class AuctionController {

    private final AuctionService auctionService;
    private final PhotoRepository photoRepository;

    @GetMapping
    public ResponseEntity<List<AuctionResponseDTO>> getAll() {
        return ResponseEntity.ok(auctionService.getAllAuctions());
    }

    @GetMapping("/{id}")
    public ResponseEntity<AuctionResponseDTO> getById(@PathVariable Integer id) {
        return ResponseEntity.ok(auctionService.getById(id));
    }

    @GetMapping("/{id}/catalog")
    public ResponseEntity<List<CatalogItemResponseDTO>> getCatalog(
            @PathVariable Integer id,
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        return ResponseEntity.ok(auctionService.getCatalog(id, userDetails != null));
    }

    @GetMapping("/items/{itemId}")
    public ResponseEntity<CatalogItemDetailDTO> getItemDetail(
            @PathVariable Integer itemId,
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        return ResponseEntity.ok(auctionService.getItemDetail(itemId, userDetails != null));
    }

    // Portada de la subasta (primera foto de cualquier producto del catálogo)
    @GetMapping("/{id}/cover")
    public ResponseEntity<byte[]> getCoverImage(@PathVariable Integer id) {
        return photoRepository.findFirstByAuctionId(id)
                .map(photo -> ResponseEntity.ok()
                        .contentType(MediaType.IMAGE_JPEG)
                        .body(photo.getImage()))
                .orElse(ResponseEntity.status(HttpStatus.NO_CONTENT).build());
    }

    // Primera foto de un ítem (thumbnail en el catálogo)
   @GetMapping("/items/{itemId}/photo")
public ResponseEntity<byte[]> getItemPhoto(@PathVariable Integer itemId) {
    return auctionService.getProductIdFromItem(itemId)
            .flatMap(photoRepository::findTopByProductIdOrderByIdAsc)
            .map(photo -> ResponseEntity.ok()
                    .contentType(MediaType.IMAGE_JPEG)
                    .body(photo.getImage()))
            .orElse(ResponseEntity.status(HttpStatus.NO_CONTENT).build());
}

    // Lista de IDs de todas las fotos de un ítem (para la galería del detalle)
    @GetMapping("/items/{itemId}/photos")
    public ResponseEntity<List<Integer>> getItemPhotoIds(@PathVariable Integer itemId) {
        return auctionService.getProductIdFromItem(itemId)
                .map(productId -> ResponseEntity.ok(photoRepository.findIdsByProductId(productId)))
                .orElse(ResponseEntity.ok(List.of()));
    }
}
