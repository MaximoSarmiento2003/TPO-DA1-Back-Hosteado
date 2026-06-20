package com.example.tpoDA.repositories;

import com.example.tpoDA.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer> {

    Optional<User> findByEmail(String email);

    Optional<User> findByClientId(Integer clientId);

    // Para buscar el usuario a partir del Owner (mismo ID que Person)
    @Query("SELECT u FROM User u WHERE u.client.person.id = :ownerId")
    Optional<User> findByOwnerId(@Param("ownerId") Integer ownerId);
}
