package com.example.tpoDA.services;

import com.example.tpoDA.dtos.bid.BidHistoryResponseDTO;
import com.example.tpoDA.dtos.bid.BidMetricsResponseDTO;
import com.example.tpoDA.entities.*;
import com.example.tpoDA.exceptions.AppException;
import com.example.tpoDA.repositories.BidRepository;
import com.example.tpoDA.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BidHistoryService {

    private final BidRepository bidRepository;
    private final UserRepository userRepository;

    // ─── Historia de pujas ────────────────────────────────────────────────────

    public List<BidHistoryResponseDTO> getHistory(String email) {
        Integer clientId = resolveClientId(email);
        return bidRepository.findByClientId(clientId)
                .stream()
                .map(this::toHistoryDTO)
                .toList();
    }

    // ─── Métricas ─────────────────────────────────────────────────────────────

    public BidMetricsResponseDTO getMetrics(String email) {
        Integer clientId = resolveClientId(email);
        List<Bid> bids = bidRepository.findByClientId(clientId);

        int totalBids = bids.size();

        // Subastas únicas en las que participó
        Set<Integer> auctionIds = bids.stream()
                .map(b -> b.getAttendee().getAuction().getId())
                .collect(Collectors.toSet());
        int auctionsAttended = auctionIds.size();

        // Subastas donde ganó al menos un ítem
        Set<Integer> wonAuctionIds = bids.stream()
                .filter(b -> "si".equalsIgnoreCase(b.getWinner()))
                .map(b -> b.getAttendee().getAuction().getId())
                .collect(Collectors.toSet());
        int auctionsWon = wonAuctionIds.size();
        int auctionsLost = auctionsAttended - auctionsWon;

        // Importes
        BigDecimal totalSpent = bids.stream()
                .filter(b -> "si".equalsIgnoreCase(b.getWinner()))
                .map(Bid::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal totalBid = bids.stream()
                .map(Bid::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return BidMetricsResponseDTO.builder()
                .totalBids(totalBids)
                .auctionsAttended(auctionsAttended)
                .auctionsWon(auctionsWon)
                .auctionsLost(auctionsLost)
                .totalSpent(totalSpent)
                .totalBid(totalBid)
                .build();
    }

    // ─── Helpers ──────────────────────────────────────────────────────────────

    private Integer resolveClientId(String email) {
        return userRepository.findByEmail(email)
                .map(u -> u.getClient().getId())
                .orElseThrow(() -> new AppException("Usuario no encontrado", HttpStatus.NOT_FOUND));
    }

    private BidHistoryResponseDTO toHistoryDTO(Bid bid) {
        Product product = bid.getItem().getProduct();
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

        Auction auction = bid.getAttendee().getAuction();

        return BidHistoryResponseDTO.builder()
                .bidId(bid.getId())
                .productName(name)
                .productBrand(brand)
                .productType(type)
                .auctionId(auction.getId())
                .auctionDate(auction.getDate())
                .auctionLocation(auction.getLocation())
                .amount(bid.getAmount())
                .winner("si".equalsIgnoreCase(bid.getWinner()) ? true
                      : "no".equalsIgnoreCase(bid.getWinner()) ? false
                      : null)
                .build();
    }
}
