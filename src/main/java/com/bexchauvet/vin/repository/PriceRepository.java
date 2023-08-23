package com.bexchauvet.vin.repository;

import com.bexchauvet.vin.domain.Price;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.Instant;

@Repository
public interface PriceRepository extends JpaRepository<Price, String> {

    @Modifying
    @Transactional
    @Query("DELETE FROM prices p WHERE p.date < :date")
    void deleteOlderThan(@Param("date") Instant date);
}