package com.example.tpoDA.repositories;

import com.example.tpoDA.entities.Person;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PersonRepository extends JpaRepository<Person, Integer> {
    boolean existsByDocument(String document);
}