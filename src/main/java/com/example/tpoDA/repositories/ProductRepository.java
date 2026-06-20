package com.example.tpoDA.repositories;

import com.example.tpoDA.entities.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Integer> {

    @Query("SELECT p FROM Product p WHERE p.owner.id = :ownerId ORDER BY p.date DESC")
    List<Product> findByOwnerId(@Param("ownerId") Integer ownerId);

    @Query("SELECT p FROM Product p WHERE p.owner.id = :ownerId AND p.available = 'si' ORDER BY p.date DESC")
    List<Product> findApprovedByOwnerId(@Param("ownerId") Integer ownerId);

    // Productos pendientes de revisión (available = 'no')
    @Query("SELECT p FROM Product p WHERE p.available = 'no' ORDER BY p.date ASC")
    List<Product> findPendingReview();
}
