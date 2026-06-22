package com.example.tpoDA.repositories;

import com.example.tpoDA.entities.CheckCommitment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface CheckCommitmentRepository extends JpaRepository<CheckCommitment, Integer> {

    Optional<CheckCommitment> findByPaymentMethodIdAndItemId(Integer paymentMethodId, Integer itemId);

    // Suma de todo lo comprometido por ese cheque en cualquier ítem (vigente o ya ganado)
    @Query("""
        SELECT COALESCE(SUM(c.amount), 0) FROM CheckCommitment c
        WHERE c.paymentMethod.id = :paymentMethodId
    """)
    java.math.BigDecimal sumCommittedByPaymentMethodId(@Param("paymentMethodId") Integer paymentMethodId);

    // Mismo cálculo, pero excluyendo el compromiso de un ítem puntual
    // (se usa para validar la nueva oferta sobre ESE ítem, sin contarla dos veces)
    @Query("""
        SELECT COALESCE(SUM(c.amount), 0) FROM CheckCommitment c
        WHERE c.paymentMethod.id = :paymentMethodId
          AND c.item.id <> :excludeItemId
    """)
    java.math.BigDecimal sumCommittedExcludingItem(@Param("paymentMethodId") Integer paymentMethodId,
                                                    @Param("excludeItemId") Integer excludeItemId);

    // Libera (borra) los compromisos de quienes ofertaron sobre el ítem pero no ganaron.
    // Los que sí quedaron "locked" (el ganador) se conservan.
    @org.springframework.data.jpa.repository.Modifying
    @Query("DELETE FROM CheckCommitment c WHERE c.item.id = :itemId AND c.locked = false")
    void releaseUnlockedForItem(@Param("itemId") Integer itemId);
}
