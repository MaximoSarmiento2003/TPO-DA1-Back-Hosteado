package com.example.tpoDA.repositories;

import com.example.tpoDA.entities.Photo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface PhotoRepository extends JpaRepository<Photo, Integer> {

    // Primera foto de cualquier producto del catálogo de la subasta (portada)
    @Query(value = """
        SELECT f.* FROM fotos f
        INNER JOIN items_catalogo ic ON ic.producto = f.producto
        INNER JOIN catalogos cat ON cat.identificador = ic.catalogo
        WHERE cat.subasta = :auctionId
        LIMIT 1
    """, nativeQuery = true)
    Optional<Photo> findFirstByAuctionId(@Param("auctionId") Integer auctionId);

    // Primera foto de un producto (thumbnail del catálogo)
    Optional<Photo> findTopByProductIdOrderByIdAsc(Integer productId);

    // Todas las fotos de un producto (galería del detalle)
    @Query("SELECT p FROM Photo p WHERE p.product.id = :productId ORDER BY p.id ASC")
    List<Photo> findAllByProductId(@Param("productId") Integer productId);

    // IDs de todas las fotos de un producto (para el front)
    @Query("SELECT p.id FROM Photo p WHERE p.product.id = :productId ORDER BY p.id ASC")
    List<Integer> findIdsByProductId(@Param("productId") Integer productId);
}
