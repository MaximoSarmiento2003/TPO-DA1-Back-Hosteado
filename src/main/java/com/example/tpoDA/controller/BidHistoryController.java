package com.example.tpoDA.controller;

import com.example.tpoDA.dtos.bid.BidHistoryResponseDTO;
import com.example.tpoDA.dtos.bid.BidMetricsResponseDTO;
import com.example.tpoDA.services.BidHistoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users/bids")
@RequiredArgsConstructor
public class BidHistoryController {

    private final BidHistoryService bidHistoryService;

    // GET /users/bids/history — historial completo de pujas
    @GetMapping("/history")
    public ResponseEntity<List<BidHistoryResponseDTO>> getHistory(Authentication auth) {
        return ResponseEntity.ok(bidHistoryService.getHistory(auth.getName()));
    }

    // GET /users/bids/metrics — métricas del usuario
    @GetMapping("/metrics")
    public ResponseEntity<BidMetricsResponseDTO> getMetrics(Authentication auth) {
        return ResponseEntity.ok(bidHistoryService.getMetrics(auth.getName()));
    }
}
