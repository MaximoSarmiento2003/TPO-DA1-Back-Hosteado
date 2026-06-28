package com.example.tpoDA.repositories;

import com.example.tpoDA.entities.Employee;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface EmployeeRepository extends JpaRepository<Employee, Integer> {
    @Query(value = "SELECT * FROM empleados ORDER BY RAND() LIMIT 1", nativeQuery = true)
    Optional<Employee> findRandom();
}
