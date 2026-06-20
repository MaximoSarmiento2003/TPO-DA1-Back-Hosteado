package com.example.tpoDA.services;

import com.example.tpoDA.entities.*;
import com.example.tpoDA.repositories.NotificationRepository;
import com.example.tpoDA.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class NotificationTriggerService {

    private final NotificationRepository notificationRepository;
    private final UserRepository userRepository;

    // ─── Producto aprobado ────────────────────────────────────────────────────
    public void notifyProductApproved(User user, Product product,
                                       BigDecimal basePrice, BigDecimal commissionRate) {
        String name = productName(product);
        String msg = String.format(
            "Tu producto \"%s\" fue aceptado. La comisión es del %.0f%% y su valor inicial es de $%,.0f.",
            name, commissionRate.doubleValue(), basePrice.doubleValue());
        save(Notification.builder()
                .user(user).product(product)
                .type("PRODUCT_APPROVED").message(msg)
                .basePrice(basePrice).commissionRate(commissionRate)
                .createdAt(LocalDateTime.now()).userResponse("PENDING").build());
    }

    // ─── Producto rechazado ───────────────────────────────────────────────────
    public void notifyProductRejected(User user, Product product, String reason) {
        String name = productName(product);
        String extra = (reason != null && !reason.isBlank()) ? " Motivo: " + reason + "." : "";
        String msg = String.format(
            "Tu producto \"%s\" no pudo ser aceptado en esta oportunidad.%s", name, extra);
        save(Notification.builder()
                .user(user).product(product)
                .type("PRODUCT_REJECTED").message(msg)
                .createdAt(LocalDateTime.now()).userResponse("PENDING").build());
    }

    // ─── Puja superada ────────────────────────────────────────────────────────
    public void notifyBidOutbid(User user, Auction auction,
                                 String itemName, BigDecimal newAmount) {
        String msg = String.format(
            "Tu oferta fue superada en \"%s\". La oferta actual es de $%,.0f.",
            itemName, newAmount.doubleValue());
        save(Notification.builder()
                .user(user).auction(auction)
                .type("BID_OUTBID").message(msg).amount(newAmount)
                .createdAt(LocalDateTime.now()).userResponse("PENDING").build());
    }

    // ─── Subasta por empezar ──────────────────────────────────────────────────
    public void notifyAuctionStarting(User user, Auction auction, int minutesLeft) {
        String loc = auction.getLocation() != null ? auction.getLocation() : "Subasta #" + auction.getId();
        String msg = String.format(
            "La subasta \"%s\" está a punto de comenzar. Tenés %d minuto%s para hacer tus ofertas.",
            loc, minutesLeft, minutesLeft == 1 ? "" : "s");
        save(Notification.builder()
                .user(user).auction(auction)
                .type("AUCTION_STARTING").message(msg).minutesToStart(minutesLeft)
                .createdAt(LocalDateTime.now()).userResponse("PENDING").build());
    }

    // ─── Pago confirmado ──────────────────────────────────────────────────────
    public void notifyPaymentSuccess(User user, BigDecimal amount, String detail) {
        String msg = String.format(
            "Pago confirmado por $%,.0f. %s", amount.doubleValue(), detail != null ? detail : "");
        save(Notification.builder()
                .user(user)
                .type("PAYMENT_SUCCESS").message(msg).amount(amount)
                .createdAt(LocalDateTime.now()).userResponse("PENDING").build());
    }

    // ─── Alerta de watchlist ──────────────────────────────────────────────────
    public void notifyWatchlistAlert(User user, Auction auction, String itemName) {
        String loc = auction.getLocation() != null ? auction.getLocation() : "una subasta";
        String msg = String.format(
            "El artículo \"%s\" que seguís aparece en %s. ¡No te lo pierdas!", itemName, loc);
        save(Notification.builder()
                .user(user).auction(auction)
                .type("WATCHLIST_ALERT").message(msg)
                .createdAt(LocalDateTime.now()).userResponse("PENDING").build());
    }

    // ─── Helpers ──────────────────────────────────────────────────────────────
    private void save(Notification n) {
        notificationRepository.save(n);
    }

    private String productName(Product p) {
        if (p instanceof NormalProduct np) return np.getName();
        if (p instanceof ArtProduct ap)    return ap.getName();
        return "Producto #" + p.getId();
    }
}
