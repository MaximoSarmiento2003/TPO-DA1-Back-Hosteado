package com.example.tpoDA.repositories;

import com.example.tpoDA.entities.CatalogItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface CatalogItemRepository extends JpaRepository<CatalogItem, Integer> {

    @Query("""
        SELECT ci FROM CatalogItem ci
        JOIN FETCH ci.product
        JOIN ci.catalog c
        WHERE c.auction.id = :auctionId
    """)
    List<CatalogItem> findByAuctionId(@Param("auctionId") Integer auctionId);

    // Con todos los datos necesarios para el detalle
    @Query("""
        SELECT ci FROM CatalogItem ci
        JOIN FETCH ci.product p
        JOIN FETCH ci.catalog cat
        JOIN FETCH cat.auction a
        LEFT JOIN FETCH a.auctioneer ae
        LEFT JOIN FETCH ae.person
        WHERE ci.id = :itemId
    """)
    Optional<CatalogItem> findDetailById(@Param("itemId") Integer itemId);
}
