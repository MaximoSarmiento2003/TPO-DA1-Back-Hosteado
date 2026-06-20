package com.example.tpoDA.repositories;

import com.example.tpoDA.entities.AuctionRecord;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuctionRecordRepository extends JpaRepository<AuctionRecord, Integer> {
}
