package com.bexchauvet.vin.repository;

import com.bexchauvet.vin.domain.Wine;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WineRepository extends JpaRepository<Wine, String> {


    List<Wine> getByCriteria(List<String> colors, List<String> country, List<Integer> vintages, Double minScore,
                             Double maxScore, Double minPrice, Double maxPrice);

}
