package com.bexchauvet.vin.repository;

import com.bexchauvet.vin.domain.TasteScore;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TasteScoreRepository extends JpaRepository<TasteScore, String> {


    @Query("SELECT t FROM TasteScore t WHERE t.expert = :user_id")
    List<TasteScore> findByUserID(@Param("user_id") String userID);

}
