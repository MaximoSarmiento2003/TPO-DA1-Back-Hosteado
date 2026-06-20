package com.example.tpoDA.services;

import com.example.tpoDA.dtos.fine.FineResponseDTO;
import com.example.tpoDA.dtos.fine.PayFineResponseDTO;
import com.example.tpoDA.entities.*;
import com.example.tpoDA.exceptions.AppException;
import com.example.tpoDA.repositories.BidRepository;
import com.example.tpoDA.repositories.ClientRepository;
import com.example.tpoDA.repositories.FineRepository;
import com.example.tpoDA.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class FineService {

    private final FineRepository fineRepository;
    private final UserRepository userRepository;
    private final BidRepository bidRepository;
    private final ClientRepository clientRepository;

    public List<FineResponseDTO> getMyFines(String email) {
        Integer clientId = resolveClientId(email);
        return fineRepository.findByClientId(clientId)
                .stream()
                .map(this::toDTO)
                .toList();
    }

    public List<FineResponseDTO> getMyPendingFines(String email) {
        Integer clientId = resolveClientId(email);
        return fineRepository.findByClientId(clientId)
                .stream()
                .map(this::toDTO)
                .toList();
    }

    @Transactional
    public FineResponseDTO createFine(Integer clientId, Integer bidId) {
        Client client = clientRepository.findById(clientId)
                .orElseThrow(() -> new AppException("Cliente no encontrado", HttpStatus.NOT_FOUND));

        Bid bid = bidRepository.findById(bidId)
                .orElseThrow(() -> new AppException("Puja no encontrada", HttpStatus.NOT_FOUND));

        BigDecimal fineAmount = bid.getAmount()
                .multiply(BigDecimal.valueOf(0.10))
                .setScale(2, RoundingMode.HALF_UP);

        Fine fine = Fine.builder()
                .client(client)
                .bid(bid)
                .amount(fineAmount)
                .dueDate(LocalDate.now().plusDays(3))
                .status("pendiente")
                .build();

        return toDTO(fineRepository.save(fine));
    }

    @Transactional
    public PayFineResponseDTO payFine(Integer fineId, String email) {
        Integer clientId = resolveClientId(email);

        Fine fine = fineRepository.findById(fineId)
                .orElseThrow(() -> new AppException("Multa no encontrada", HttpStatus.NOT_FOUND));

        if (!fine.getClient().getId().equals(clientId)) {
            throw new AppException("No tenés permiso para pagar esta multa", HttpStatus.FORBIDDEN);
        }

        if ("pagada".equals(fine.getStatus())) {
            throw new AppException("Esta multa ya fue pagada", HttpStatus.BAD_REQUEST);
        }

        fine.setStatus("pagada");
        fineRepository.save(fine);

        boolean stillPending = fineRepository.existsByClientIdAndStatus(clientId, "pendiente");

        return PayFineResponseDTO.builder()
                .message("Multa pagada correctamente")
                .hasPendingFines(stillPending)
                .build();
    }

    private Integer resolveClientId(String email) {
        return userRepository.findByEmail(email)
                .map(u -> u.getClient().getId())
                .orElseThrow(() -> new AppException("Usuario no encontrado", HttpStatus.NOT_FOUND));
    }

    private FineResponseDTO toDTO(Fine fine) {
        Bid bid = fine.getBid();
        Product product = bid.getItem().getProduct();

        String productName = "";
        if (product instanceof NormalProduct np) {
            productName = np.getName();
        } else if (product instanceof ArtProduct ap) {
            productName = ap.getName();
        }

        return FineResponseDTO.builder()
                .id(fine.getId())
                .amount(fine.getAmount())
                .dueDate(fine.getDueDate())
                .status(fine.getStatus())
                .bidId(bid.getId())
                .bidAmount(bid.getAmount())
                .productName(productName)
                .auctionId(bid.getAttendee().getAuction().getId())
                .build();
    }
}