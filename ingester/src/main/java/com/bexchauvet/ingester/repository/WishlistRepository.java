package com.bexchauvet.ingester.repository;

import com.bexchauvet.ingester.domain.WishList;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WishlistRepository extends JpaRepository<WishList, String> {

    @Query("SELECT w FROM WishList w WHERE w.user = :user_id")
    List<WishList> findByUserID(@Param("user_id") String userID);

}
