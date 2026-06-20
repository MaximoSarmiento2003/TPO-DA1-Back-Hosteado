package com.example.tpoDA.services;

import com.example.tpoDA.dtos.live.*;
import com.example.tpoDA.entities.*;
import com.example.tpoDA.exceptions.AppException;
import com.example.tpoDA.repositories.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class LiveAuctionService {

    private final UserRepository               userRepository;
    private final AuctionRepository            auctionRepository;
    private final AttendeeRepository           attendeeRepository;
    private final AttendeeSessionRepository    attendeeSessionRepository;
    private final CatalogItemRepository        catalogItemRepository;
    private final BidRepository                bidRepository;
    private final FineRepository               fineRepository;
    private final PaymentMethodRepository      paymentMethodRepository;
    private final AuctionRecordRepository      auctionRecordRepository;
    private final EmailService                 emailService;
    private final NotificationTriggerService   notificationTrigger;
    private final SimpMessagingTemplate        messagingTemplate;
    private final OwnerRepository              ownerRepository;
    private final ProductRepository            productRepository;

    // ─── UNIRSE A LA SUBASTA ─────────────────────────────────────────────────

    @Transactional
    public JoinAuctionResponseDTO joinAuction(String email, Integer auctionId,
                                               JoinAuctionRequestDTO req) {
        User user = resolveUser(email);
        Client client = user.getClient();
        Auction auction = resolveAuction(auctionId);

        // 1. Validaciones de acceso
        validateCanJoin(client, auction);

        // 2. Un usuario no puede estar en más de una subasta a la vez
        attendeeSessionRepository.findByUserId(user.getId()).ifPresent(existing -> {
            if (!existing.getAuction().getId().equals(auctionId)) {
                throw new AppException(
                    "Ya estás conectado a otra subasta. Salí primero de esa.",
                    HttpStatus.CONFLICT);
            }
        });

        // 3. Resolver/crear Attendee
        Attendee attendee = attendeeRepository
            .findByClientIdAndAuctionId(client.getId(), auctionId)
            .orElseGet(() -> {
                int bidderNum =
    attendeeRepository.countByAuctionId(auctionId) + 1;
                return attendeeRepository.save(Attendee.builder()
                    .client(client)
                    .auction(auction)
                    .bidderNumber(bidderNum)
                    .build());
            });

        // 4. Seleccionar medio de pago
        PaymentMethod pm = null;
        boolean canBid = false;
        if (req.getPaymentMethodId() != null) {
            pm = paymentMethodRepository.findById(req.getPaymentMethodId())
                .orElseThrow(() -> new AppException("Medio de pago no encontrado", HttpStatus.NOT_FOUND));
            if (!pm.getUser().getId().equals(user.getId()))
                throw new AppException("El medio de pago no te pertenece", HttpStatus.FORBIDDEN);
            if (!Boolean.TRUE.equals(pm.getVerificado()))
                throw new AppException("El medio de pago no está verificado", HttpStatus.BAD_REQUEST);
            if (fineRepository.existsByClientIdAndStatus(client.getId(), "pendiente"))
                throw new AppException("Tenés multas pendientes. Regularizalas antes de pujar.",
                    HttpStatus.FORBIDDEN);
            canBid = true;
        }

        // 5. Guardar sesión activa
        PaymentMethod finalPm = pm;
        AttendeeSession session = attendeeSessionRepository.findByUserId(user.getId())
            .map(s -> { s.setAuction(auction); s.setPaymentMethod(finalPm);
                        s.setActive(true); return s; })
            .orElse(AttendeeSession.builder()
                .user(user).auction(auction).paymentMethod(finalPm)
                .joinedAt(LocalDateTime.now()).active(true).build());
        attendeeSessionRepository.save(session);

        // 6. Ítem actual (primer ítem no subastado)
        CatalogItem currentItem = getCurrentItem(auctionId);
        BigDecimal currentBid = currentItem != null
            ? bidRepository.findTopBidByItemId(currentItem.getId())
                .map(Bid::getAmount).orElse(null)
            : null;

        // 7. Mandar mail con link de streaming
        try {
            emailService.sendStreamingEmail(user.getEmail(),
                auction.getLocation(), "https://streaming.auctionpro.com/live/" + auctionId);
        } catch (Exception e) {
            System.err.println("[LiveAuction] No se pudo enviar mail de streaming: " + e.getMessage());
        }

        return JoinAuctionResponseDTO.builder()
            .auctionId(auctionId)
            .currentItemId(currentItem != null ? currentItem.getId() : null)
            .currentItemName(currentItem != null ? getProductName(currentItem.getProduct()) : null)
            .currentItemDescription(currentItem != null
                ? currentItem.getProduct().getCatalogDescription() : null)
            .basePrice(currentItem != null ? currentItem.getBasePrice() : null)
            .currentBid(currentBid)
            .bidderNumber(attendee.getBidderNumber())
            .canBid(canBid)
            .streamingLink("https://streaming.auctionpro.com/live/" + auctionId)
            .currency("ARS")
            .build();
    }

    // ─── REALIZAR OFERTA ─────────────────────────────────────────────────────

    @Transactional
    public synchronized BidUpdateDTO placeBid(String email, PlaceBidRequestDTO req) {
        User user = resolveUser(email);
        Client client = user.getClient();
        Auction auction = resolveAuction(req.getAuctionId());
        CatalogItem item = catalogItemRepository.findById(req.getItemId())
            .orElseThrow(() -> new AppException("Ítem no encontrado", HttpStatus.NOT_FOUND));

        // Validaciones
        if (!"si".equals(item.getAuctioned()) == false && "si".equals(item.getAuctioned()))
            throw new AppException("Este artículo ya fue subastado", HttpStatus.CONFLICT);

        AttendeeSession session = attendeeSessionRepository.findByUserId(user.getId())
            .orElseThrow(() -> new AppException("No estás conectado a esta subasta", HttpStatus.FORBIDDEN));
        if (!session.getAuction().getId().equals(req.getAuctionId()))
            throw new AppException("No estás conectado a esta subasta", HttpStatus.FORBIDDEN);
        if (session.getPaymentMethod() == null)
            throw new AppException("No seleccionaste un medio de pago", HttpStatus.BAD_REQUEST);
        if (fineRepository.existsByClientIdAndStatus(client.getId(), "pendiente"))
            throw new AppException("Tenés multas pendientes", HttpStatus.FORBIDDEN);

        Attendee attendee = attendeeRepository
            .findByClientIdAndAuctionId(client.getId(), req.getAuctionId())
            .orElseThrow(() -> new AppException("No sos asistente de esta subasta", HttpStatus.FORBIDDEN));

        // Validar límites de oferta
        BigDecimal basePrice = item.getBasePrice();
        Optional<Bid> topBidOpt = bidRepository.findTopBidByItemId(item.getId());
        BigDecimal currentTop = topBidOpt.map(Bid::getAmount).orElse(BigDecimal.ZERO);
        BigDecimal amount = req.getAmount();

        if (amount.compareTo(currentTop) <= 0)
            throw new AppException("La oferta debe ser mayor a la oferta actual", HttpStatus.BAD_REQUEST);

        ClientCategory cat = auction.getCategory();
        boolean hasLimits = cat == ClientCategory.COMUN
            || cat == ClientCategory.ESPECIAL
            || cat == ClientCategory.PLATA;

        if (hasLimits) {
            BigDecimal minBid = currentTop.add(basePrice.multiply(BigDecimal.valueOf(0.01)));
            BigDecimal maxBid = currentTop.add(basePrice.multiply(BigDecimal.valueOf(0.20)));
            if (amount.compareTo(minBid) < 0)
                throw new AppException(
                    "Oferta mínima: $" + minBid.setScale(2, RoundingMode.HALF_UP), HttpStatus.BAD_REQUEST);
            if (amount.compareTo(maxBid) > 0)
                throw new AppException(
                    "Oferta máxima: $" + maxBid.setScale(2, RoundingMode.HALF_UP), HttpStatus.BAD_REQUEST);
        }

        // Marcar oferta anterior como superada
        topBidOpt.ifPresent(prev -> {
            prev.setWinner("no");
            bidRepository.save(prev);
        });

        // Registrar nueva oferta
        Bid bid = bidRepository.save(Bid.builder()
            .attendee(attendee)
            .item(item)
            .amount(amount)
            .winner("si")
            .build());

        // Calcular próximos límites
        BigDecimal nextMin = amount.add(basePrice.multiply(BigDecimal.valueOf(0.01)));
        BigDecimal nextMax = hasLimits
            ? amount.add(basePrice.multiply(BigDecimal.valueOf(0.20))) : null;

        BidUpdateDTO update = BidUpdateDTO.builder()
            .bidId(bid.getId())
            .itemId(item.getId())
            .auctionId(req.getAuctionId())
            .amount(amount)
            .bidderNumber(attendee.getBidderNumber())
            .timestamp(LocalDateTime.now())
            .minNextBid(nextMin)
            .maxNextBid(nextMax)
            .build();

        // Broadcast en tiempo real
        messagingTemplate.convertAndSend(
            "/topic/auction/" + req.getAuctionId() + "/bids", update);

        return update;
    }

    // ─── CERRAR ÍTEM (rematador) ──────────────────────────────────────────────

    @Transactional
    public ItemResultDTO closeItem(Integer auctionId, Integer itemId) {
        resolveAuction(auctionId);
        CatalogItem item = catalogItemRepository.findById(itemId)
            .orElseThrow(() -> new AppException("Ítem no encontrado", HttpStatus.NOT_FOUND));

        Optional<Bid> winnerBidOpt = bidRepository.findTopBidByItemId(itemId);
        boolean sold = winnerBidOpt.isPresent();
        BigDecimal finalAmount = item.getBasePrice();
        Integer winnerBidderNumber = null;
        Client buyer = null;

        if (sold) {
            Bid winnerBid = winnerBidOpt.get();
            finalAmount = winnerBid.getAmount();
            winnerBidderNumber = winnerBid.getAttendee().getBidderNumber();
            buyer = winnerBid.getAttendee().getClient();

            // Registrar venta
            BigDecimal commission = finalAmount.multiply(
                item.getCommission().divide(BigDecimal.valueOf(100)));
            BigDecimal shipping = BigDecimal.valueOf(500); // tarifa fija de ejemplo

            auctionRecordRepository.save(AuctionRecord.builder()
                .auction(item.getCatalog().getAuction())
                .owner(item.getProduct().getOwner())
                .product(item.getProduct())
                .client(buyer)
                .amount(finalAmount)
                .commission(commission)
                .build());

            // Transferir propiedad
            Product product = item.getProduct();
Person buyerPerson = buyer.getPerson();

Owner newOwner = ownerRepository.findById(buyerPerson.getId())
    .orElse(null);

if (newOwner == null) {
    newOwner = Owner.builder()
        .person(buyerPerson)
        .country(buyer.getCountry())
        .verifier(buyer.getVerifier())
        .financialVerification("si")
        .judicialVerification("si")
        .riskRating(1)
        .build();

    newOwner = ownerRepository.save(newOwner);
}

product.setOwner(newOwner);
productRepository.save(product); // lazy load ok
            

            // Notificación al ganador
            User winnerUser = userRepository.findByOwnerId(buyer.getPerson().getId())
                .orElse(null);
            if (winnerUser != null) {
                notificationTrigger.notifyPaymentSuccess(winnerUser,
                    finalAmount.add(commission).add(shipping),
                    String.format("Ganaste el artículo \"%s\". Comisión: $%,.0f · Envío: $%,.0f",
                        getProductName(item.getProduct()), commission.doubleValue(), shipping.doubleValue()));
            }
        }

        // Marcar ítem como subastado
        item.setAuctioned("si");
        catalogItemRepository.save(item);

        // Siguiente ítem
        CatalogItem nextItem = getNextItem(auctionId, itemId);
        boolean auctionFinished = nextItem == null;

        // Si terminó la subasta, marcarla como cerrada
        if (auctionFinished) {
            Auction auction = resolveAuction(auctionId);
            auction.setStatus(AuctionStatus.CERRADA);
            auctionRepository.save(auction);
        }

        ItemResultDTO result = ItemResultDTO.builder()
            .itemId(itemId)
            .auctionId(auctionId)
            .itemName(getProductName(item.getProduct()))
            .sold(sold)
            .winnerBidderNumber(winnerBidderNumber)
            .finalAmount(finalAmount)
            .commission(item.getCommission())
            .shippingCost(BigDecimal.valueOf(500))
            .nextItemId(nextItem != null ? nextItem.getId() : null)
            .nextItemName(nextItem != null ? getProductName(nextItem.getProduct()) : null)
            .nextBasePrice(nextItem != null ? nextItem.getBasePrice() : null)
            .auctionFinished(auctionFinished)
            .build();

        // Broadcast resultado
        messagingTemplate.convertAndSend("/topic/auction/" + auctionId + "/result", result);

        return result;
    }

    // ─── SALIR DE LA SUBASTA ─────────────────────────────────────────────────

    @Transactional
    public void leaveAuction(String email, Integer auctionId) {
        User user = resolveUser(email);
        attendeeSessionRepository.findByUserId(user.getId()).ifPresent(session -> {
            session.setActive(false);
            attendeeSessionRepository.save(session);
        });
    }

    // ─── ESTADO ACTUAL (para reconexión) ─────────────────────────────────────

    public JoinAuctionResponseDTO getAuctionState(String email, Integer auctionId) {
        User user = resolveUser(email);
        Client client = user.getClient();

        AttendeeSession session = attendeeSessionRepository.findByUserId(user.getId())
            .filter(s -> s.getAuction().getId().equals(auctionId))
            .orElseThrow(() -> new AppException("No estás conectado a esta subasta", HttpStatus.FORBIDDEN));

        Attendee attendee = attendeeRepository
            .findByClientIdAndAuctionId(client.getId(), auctionId)
            .orElseThrow(() -> new AppException("No sos asistente", HttpStatus.FORBIDDEN));

        CatalogItem currentItem = getCurrentItem(auctionId);
        BigDecimal currentBid = currentItem != null
            ? bidRepository.findTopBidByItemId(currentItem.getId())
                .map(Bid::getAmount).orElse(null)
            : null;

        return JoinAuctionResponseDTO.builder()
            .auctionId(auctionId)
            .currentItemId(currentItem != null ? currentItem.getId() : null)
            .currentItemName(currentItem != null ? getProductName(currentItem.getProduct()) : null)
            .currentItemDescription(currentItem != null
                ? currentItem.getProduct().getCatalogDescription() : null)
            .basePrice(currentItem != null ? currentItem.getBasePrice() : null)
            .currentBid(currentBid)
            .bidderNumber(attendee.getBidderNumber())
            .canBid(session.getPaymentMethod() != null)
            .streamingLink("https://streaming.auctionpro.com/live/" + auctionId)
            .currency("ARS")
            .build();
    }

    // ─── HISTORIAL DE PUJAS DEL ÍTEM ─────────────────────────────────────────

    public List<BidUpdateDTO> getItemBidHistory(Integer itemId) {
        return bidRepository.findByItemIdOrderByIdAsc(itemId).stream().map(b ->
            BidUpdateDTO.builder()
                .bidId(b.getId())
                .itemId(b.getItem().getId())
                .auctionId(b.getItem().getCatalog().getAuction().getId())
                .amount(b.getAmount())
                .bidderNumber(b.getAttendee().getBidderNumber())
                .timestamp(LocalDateTime.now())
                .build()
        ).toList();
    }

    // ─── HELPERS ─────────────────────────────────────────────────────────────

    private void validateCanJoin(Client client, Auction auction) {
        if (!"si".equalsIgnoreCase(client.getAdmitted()))
            throw new AppException("Tu cuenta no está aprobada por la empresa", HttpStatus.FORBIDDEN);

        ClientCategory[] order = ClientCategory.values();
        int userOrd = client.getCategory() != null
            ? client.getCategory().ordinal() : 0;
        int auctionOrd = auction.getCategory() != null
            ? auction.getCategory().ordinal() : 0;

        if (userOrd < auctionOrd)
            throw new AppException(
                "Tu categoría no es suficiente para esta subasta. Requiere: "
                    + auction.getCategory().name(), HttpStatus.FORBIDDEN);

        if (auction.getStatus() != AuctionStatus.ABIERTA)
            throw new AppException("La subasta no está activa", HttpStatus.BAD_REQUEST);
    }

    private CatalogItem getCurrentItem(Integer auctionId) {
        return catalogItemRepository.findByAuctionId(auctionId).stream()
            .filter(ci -> !"si".equals(ci.getAuctioned()))
            .findFirst().orElse(null);
    }

    private CatalogItem getNextItem(Integer auctionId, Integer currentItemId) {
        List<CatalogItem> items = catalogItemRepository.findByAuctionId(auctionId);
        boolean found = false;
        for (CatalogItem ci : items) {
            if (found && !"si".equals(ci.getAuctioned())) return ci;
            if (ci.getId().equals(currentItemId)) found = true;
        }
        return null;
    }

    private User resolveUser(String email) {
        return userRepository.findByEmail(email)
            .orElseThrow(() -> new AppException("Usuario no encontrado", HttpStatus.NOT_FOUND));
    }

    private Auction resolveAuction(Integer id) {
        return auctionRepository.findById(id)
            .orElseThrow(() -> new AppException("Subasta no encontrada", HttpStatus.NOT_FOUND));
    }

    private String getProductName(Product p) {
        if (p instanceof NormalProduct np) return np.getName();
        if (p instanceof ArtProduct ap)    return ap.getName();
        return "Producto #" + p.getId();
    }
}
