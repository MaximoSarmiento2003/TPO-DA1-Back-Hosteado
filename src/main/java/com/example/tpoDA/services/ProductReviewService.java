package com.example.tpoDA.services;

import com.example.tpoDA.dtos.notification.NotificationResponseDTO;
import com.example.tpoDA.dtos.product.ProductApproveDTO;
import com.example.tpoDA.dtos.product.ProductRejectDTO;
import com.example.tpoDA.dtos.product.ProductResponseDTO;
import com.example.tpoDA.entities.*;
import com.example.tpoDA.exceptions.AppException;
import com.example.tpoDA.repositories.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductReviewService {

    private final ProductRepository productRepository;
    private final UserRepository userRepository;
    private final NotificationRepository notificationRepository;
    private final NotificationTriggerService notificationTriggerService;
    private final NotificationService notificationService;

    public List<ProductResponseDTO> getPendingProducts() {
        return productRepository.findPendingReview()
                .stream().map(this::toProductDTO).toList();
    }

    @Transactional
    public NotificationResponseDTO approveProduct(Integer productId, ProductApproveDTO dto) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new AppException("Producto no encontrado", HttpStatus.NOT_FOUND));
        if ("si".equals(product.getAvailable()))
            throw new AppException("El producto ya fue aprobado", HttpStatus.CONFLICT);

        product.setAvailable("si");
        productRepository.save(product);

        User user = userRepository.findByOwnerId(product.getOwner().getId())
                .orElseThrow(() -> new AppException("Usuario no encontrado", HttpStatus.NOT_FOUND));

        BigDecimal commission = dto.getCommissionRate() != null ? dto.getCommissionRate() : BigDecimal.valueOf(10);
        BigDecimal basePrice  = dto.getBasePrice()      != null ? dto.getBasePrice()      : BigDecimal.ZERO;

        notificationTriggerService.notifyProductApproved(user, product, basePrice, commission);

        // Devolver la notificación recién creada
        return notificationRepository.findByProductIdAndUserId(productId, user.getId())
                .map(notificationService::toDTO)
                .orElseThrow();
    }

    @Transactional
    public NotificationResponseDTO rejectProduct(Integer productId, ProductRejectDTO dto) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new AppException("Producto no encontrado", HttpStatus.NOT_FOUND));

        product.setAvailable("rechazado");
        productRepository.save(product);

        User user = userRepository.findByOwnerId(product.getOwner().getId())
                .orElseThrow(() -> new AppException("Usuario no encontrado", HttpStatus.NOT_FOUND));

        notificationTriggerService.notifyProductRejected(user, product, dto.getReason());

        return notificationRepository.findByProductIdAndUserId(productId, user.getId())
                .map(notificationService::toDTO)
                .orElseThrow();
    }

    @Transactional
    public NotificationResponseDTO respondToApproval(Integer notificationId, String response, String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new AppException("Usuario no encontrado", HttpStatus.NOT_FOUND));

        com.example.tpoDA.entities.Notification notification = notificationRepository.findById(notificationId)
                .orElseThrow(() -> new AppException("Notificación no encontrada", HttpStatus.NOT_FOUND));

        if (!notification.getUser().getId().equals(user.getId()))
            throw new AppException("Sin permiso", HttpStatus.FORBIDDEN);
        if (!"PENDING".equals(notification.getUserResponse()))
            throw new AppException("Ya respondiste esta notificación", HttpStatus.CONFLICT);
        if (!"PRODUCT_APPROVED".equals(notification.getType()))
            throw new AppException("Esta notificación no requiere respuesta", HttpStatus.BAD_REQUEST);

        notification.setUserResponse(response);
        notification.setReadAt(LocalDateTime.now());

        if ("REJECTED_BY_USER".equals(response) && notification.getProduct() != null) {
            notification.getProduct().setAvailable("rechazado_por_usuario");
            productRepository.save(notification.getProduct());
        }

        notificationRepository.save(notification);
        return notificationService.toDTO(notification);
    }

    private String getProductName(Product p) {
        if (p instanceof NormalProduct np) return np.getName();
        if (p instanceof ArtProduct ap)    return ap.getName();
        return "Producto #" + p.getId();
    }

    private ProductResponseDTO toProductDTO(Product p) {
        String type = "normal", name = "", brand = null;
        if (p instanceof NormalProduct np) { name = np.getName(); brand = np.getBrand(); }
        else if (p instanceof ArtProduct ap) { type = "art"; name = ap.getName(); brand = ap.getBrand(); }
        return ProductResponseDTO.builder()
                .id(p.getId()).type(type).name(name).brand(brand)
                .catalogDescription(p.getCatalogDescription())
                .date(p.getDate()).available(p.getAvailable())
                .photoCount(p.getPhotos() != null ? p.getPhotos().size() : 0)
                .build();
    }
}
