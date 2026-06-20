package com.example.tpoDA.controller;

import com.example.tpoDA.dtos.live.*;
import com.example.tpoDA.services.LiveAuctionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class LiveAuctionController {

    private final LiveAuctionService liveAuctionService;

    // Unirse a la subasta
    @PostMapping("/auctions/{auctionId}/join")
    public ResponseEntity<JoinAuctionResponseDTO> join(
            @PathVariable Integer auctionId,
            @RequestBody(required = false) JoinAuctionRequestDTO req,
            Authentication auth
    ) {
        return ResponseEntity.ok(
            liveAuctionService.joinAuction(auth.getName(), auctionId,
                req != null ? req : new JoinAuctionRequestDTO()));
    }

    // Recuperar estado (reconexión)
    @GetMapping("/auctions/{auctionId}/state")
    public ResponseEntity<JoinAuctionResponseDTO> getState(
            @PathVariable Integer auctionId,
            Authentication auth
    ) {
        return ResponseEntity.ok(liveAuctionService.getAuctionState(auth.getName(), auctionId));
    }

    // Salir
    @PostMapping("/auctions/{auctionId}/leave")
    public ResponseEntity<Void> leave(@PathVariable Integer auctionId, Authentication auth) {
        liveAuctionService.leaveAuction(auth.getName(), auctionId);
        return ResponseEntity.noContent().build();
    }

    // Pujar via REST (con confirmación)
    @PostMapping("/auctions/{auctionId}/bid")
    public ResponseEntity<BidUpdateDTO> placeBid(
            @PathVariable Integer auctionId,
            @RequestBody PlaceBidRequestDTO req,
            Authentication auth
    ) {
        req.setAuctionId(auctionId);
        return ResponseEntity.ok(liveAuctionService.placeBid(auth.getName(), req));
    }

    // Historial de pujas de un ítem
    @GetMapping("/auctions/items/{itemId}/bids")
    public ResponseEntity<List<BidUpdateDTO>> getItemBids(@PathVariable Integer itemId) {
        return ResponseEntity.ok(liveAuctionService.getItemBidHistory(itemId));
    }

    // Cerrar ítem (rematador / admin)
    @PostMapping("/auctions/{auctionId}/items/{itemId}/close")
    public ResponseEntity<ItemResultDTO> closeItem(
            @PathVariable Integer auctionId,
            @PathVariable Integer itemId
    ) {
        return ResponseEntity.ok(liveAuctionService.closeItem(auctionId, itemId));
    }

    // WebSocket: oferta desde cliente STOMP
    @MessageMapping("/bid")
    public void placeBidWs(PlaceBidRequestDTO req, SimpMessageHeaderAccessor headers) {
        String email = headers.getUser() != null ? headers.getUser().getName() : null;
        if (email != null) liveAuctionService.placeBid(email, req);
    }
}
