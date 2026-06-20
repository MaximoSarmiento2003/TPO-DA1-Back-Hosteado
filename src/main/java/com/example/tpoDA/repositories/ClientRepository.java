package com.example.tpoDA.repositories;

import com.example.tpoDA.entities.Client;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClientRepository extends JpaRepository<Client, Integer> {
}

