package com.bexchauvet.lib.repository;

import com.bexchauvet.lib.domain.TasteScore;
import com.bexchauvet.lib.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TasteScoreRepository extends JpaRepository<TasteScore, String> {


    @Query("SELECT t FROM TasteScore t WHERE t.expert = :user_id")
    List<TasteScore> findByUser(@Param("user_id") User user);

}
