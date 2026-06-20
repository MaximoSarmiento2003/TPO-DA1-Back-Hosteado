package com.example.tpoDA.repositories;

import com.example.tpoDA.entities.Auction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AuctionRepository extends JpaRepository<Auction, Integer> {

    List<Auction> findAllByOrderByDateDescTimeDesc();
}