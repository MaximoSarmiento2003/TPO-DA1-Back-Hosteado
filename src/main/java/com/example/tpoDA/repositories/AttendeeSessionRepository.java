package com.example.tpoDA.repositories;

import com.example.tpoDA.entities.AttendeeSession;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface AttendeeSessionRepository extends JpaRepository<AttendeeSession, Integer> {
    Optional<AttendeeSession> findByUserId(Integer userId);
    void deleteByUserId(Integer userId);
}
