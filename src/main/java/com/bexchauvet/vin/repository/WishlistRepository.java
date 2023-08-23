package com.bexchauvet.vin.repository;

import com.bexchauvet.vin.domain.WishList;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WishlistRepository extends JpaRepository<WishList, String> {
}
