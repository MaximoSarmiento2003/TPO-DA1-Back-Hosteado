package com.example.tpoDA.repositories;

import com.example.tpoDA.entities.Bid;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;
import java.util.Optional;

public interface BidRepository extends JpaRepository<Bid, Integer> {

    // Mejor oferta actual de un ítem (la más alta)
    @Query("SELECT b FROM Bid b WHERE b.item.id = :itemId ORDER BY b.amount DESC")
    List<Bid> findByItemIdOrderByAmountDesc(@Param("itemId") Integer itemId);

    default Optional<Bid> findTopBidByItemId(Integer itemId) {
        var list = findByItemIdOrderByAmountDesc(itemId);
        return list.isEmpty() ? Optional.empty() : Optional.of(list.get(0));
    }

    // Historial de pujas de un ítem (en orden cronológico)
    @Query("""
        SELECT b FROM Bid b
        JOIN FETCH b.attendee a
        WHERE b.item.id = :itemId
        ORDER BY b.id ASC
    """)
    List<Bid> findByItemIdOrderByIdAsc(@Param("itemId") Integer itemId);

    // Pujas del usuario en una subasta
    @Query("""
        SELECT b FROM Bid b
        JOIN FETCH b.item ci
        JOIN FETCH ci.catalog cat
        WHERE b.attendee.client.id = :clientId
          AND cat.auction.id = :auctionId
        ORDER BY b.id ASC
    """)
    List<Bid> findByClientIdAndAuctionId(@Param("clientId") Integer clientId, @Param("auctionId") Integer auctionId);

    // Todas las pujas de un cliente (historial general)
    @Query("""
        SELECT b FROM Bid b
        JOIN FETCH b.attendee a
        JOIN FETCH a.auction auction
        JOIN FETCH b.item ci
        JOIN FETCH ci.product p
        JOIN FETCH ci.catalog cat
        WHERE a.client.id = :clientId
        ORDER BY b.id DESC
    """)
    List<Bid> findByClientId(@Param("clientId") Integer clientId);
}
