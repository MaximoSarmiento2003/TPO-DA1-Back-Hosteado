package com.example.tpoDA.repositories;

import com.example.tpoDA.entities.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface NotificationRepository extends JpaRepository<Notification, Integer> {

    @Query("SELECT n FROM Notification n WHERE n.user.id = :userId ORDER BY n.createdAt DESC")
    List<Notification> findByUserId(@Param("userId") Integer userId);

    // Recientes: producto aprobado/rechazado + pujas + pagos (últimos 7 días)
    @Query("""
        SELECT n FROM Notification n
        WHERE n.user.id = :userId
          AND n.type IN ('PRODUCT_APPROVED','PRODUCT_REJECTED','BID_OUTBID','PAYMENT_SUCCESS')
        ORDER BY n.createdAt DESC
    """)
    List<Notification> findRecentByUserId(@Param("userId") Integer userId);

    // Otras: alertas de subasta y watchlist
    @Query("""
        SELECT n FROM Notification n
        WHERE n.user.id = :userId
          AND n.type IN ('AUCTION_STARTING','WATCHLIST_ALERT')
        ORDER BY n.createdAt DESC
    """)
    List<Notification> findOthersByUserId(@Param("userId") Integer userId);

    @Query("SELECT COUNT(n) FROM Notification n WHERE n.user.id = :userId AND n.readAt IS NULL")
    long countUnreadByUserId(@Param("userId") Integer userId);

    Optional<Notification> findByProductIdAndUserId(Integer productId, Integer userId);
}
