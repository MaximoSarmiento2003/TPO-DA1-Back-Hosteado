package com.example.tpoDA.repositories;

import com.example.tpoDA.entities.Client;
import com.example.tpoDA.entities.Publicacion;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PublicacionRepository extends JpaRepository<Publicacion, Integer> {

    List<Publicacion> findByClient(Client client);
}
