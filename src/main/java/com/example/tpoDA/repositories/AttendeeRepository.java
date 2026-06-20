package com.example.tpoDA.repositories;

import com.example.tpoDA.entities.Attendee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.Optional;

public interface AttendeeRepository extends JpaRepository<Attendee, Integer> {
    Optional<Attendee> findByClientIdAndAuctionId(Integer clientId, Integer auctionId);
    boolean existsByClientIdAndAuctionId(Integer clientId, Integer auctionId);
    int countByAuctionId(Integer auctionId);
}
