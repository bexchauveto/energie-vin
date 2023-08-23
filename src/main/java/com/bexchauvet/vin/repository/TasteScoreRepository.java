package com.bexchauvet.vin.repository;

import com.bexchauvet.vin.domain.TasteScore;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TasteScoreRepository extends JpaRepository<TasteScore, String> {
}
