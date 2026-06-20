package com.example.tpoDA.repositories;

import com.example.tpoDA.entities.PaymentMethod;
import com.example.tpoDA.entities.User;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface PaymentMethodRepository extends JpaRepository<PaymentMethod, Integer> {
    List<PaymentMethod> findByUserId(Integer userId);

    List<PaymentMethod> findByUser(User user);

    Optional<PaymentMethod> findByIdAndUser(Integer id, User user);
}
