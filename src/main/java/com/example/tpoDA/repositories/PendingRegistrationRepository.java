package com.example.tpoDA.repositories;

import com.example.tpoDA.entities.PendingRegistration;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PendingRegistrationRepository extends JpaRepository<PendingRegistration, Integer> {

    Optional<PendingRegistration> findByToken(String token);

    Optional<PendingRegistration> findByUserId(Integer userId);
}
