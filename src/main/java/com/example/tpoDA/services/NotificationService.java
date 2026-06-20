package com.example.tpoDA.services;

import com.example.tpoDA.dtos.notification.NotificationResponseDTO;
import com.example.tpoDA.entities.*;
import com.example.tpoDA.exceptions.AppException;
import com.example.tpoDA.repositories.NotificationRepository;
import com.example.tpoDA.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class NotificationService {

    private final NotificationRepository notificationRepository;
    private final UserRepository userRepository;

    public List<NotificationResponseDTO> getMyNotifications(String email) {
        User user = resolve(email);
        return notificationRepository.findByUserId(user.getId())
                .stream().map(this::toDTO).toList();
    }

    public List<NotificationResponseDTO> getRecent(String email) {
        User user = resolve(email);
        return notificationRepository.findRecentByUserId(user.getId())
                .stream().map(this::toDTO).toList();
    }

    public List<NotificationResponseDTO> getOthers(String email) {
        User user = resolve(email);
        return notificationRepository.findOthersByUserId(user.getId())
                .stream().map(this::toDTO).toList();
    }

    public Map<String, Long> getUnreadCount(String email) {
        User user = resolve(email);
        return Map.of("unread", notificationRepository.countUnreadByUserId(user.getId()));
    }

    @Transactional
    public void markAsRead(Integer notificationId, String email) {
        User user = resolve(email);
        Notification n = notificationRepository.findById(notificationId)
                .orElseThrow(() -> new AppException("Notificación no encontrada", HttpStatus.NOT_FOUND));
        if (!n.getUser().getId().equals(user.getId()))
            throw new AppException("Sin permiso", HttpStatus.FORBIDDEN);
        if (n.getReadAt() == null) {
            n.setReadAt(LocalDateTime.now());
            notificationRepository.save(n);
        }
    }

    private User resolve(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new AppException("Usuario no encontrado", HttpStatus.NOT_FOUND));
    }

    public NotificationResponseDTO toDTO(Notification n) {
        String productName = "";
        String auctionLocation = null;
        if (n.getProduct() != null) {
            Product p = n.getProduct();
            if (p instanceof NormalProduct np) productName = np.getName();
            else if (p instanceof ArtProduct ap) productName = ap.getName();
        }
        if (n.getAuction() != null) auctionLocation = n.getAuction().getLocation();

        return NotificationResponseDTO.builder()
                .id(n.getId())
                .type(n.getType())
                .message(n.getMessage())
                .basePrice(n.getBasePrice())
                .commissionRate(n.getCommissionRate())
                .amount(n.getAmount())
                .minutesToStart(n.getMinutesToStart())
                .productId(n.getProduct() != null ? n.getProduct().getId() : null)
                .productName(productName)
                .auctionId(n.getAuction() != null ? n.getAuction().getId() : null)
                .auctionLocation(auctionLocation)
                .createdAt(n.getCreatedAt())
                .read(n.isRead())
                .userResponse(n.getUserResponse())
                .build();
    }
}
