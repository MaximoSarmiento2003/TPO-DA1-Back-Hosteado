package com.example.tpoDA.repositories;

import com.example.tpoDA.entities.Fine;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface FineRepository extends JpaRepository<Fine, Integer> {
    List<Fine> findByClientId(Integer clientId);
    boolean existsByClientIdAndStatus(Integer clientId, String status);
}

