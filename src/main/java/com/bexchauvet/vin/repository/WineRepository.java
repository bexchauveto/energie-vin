package com.bexchauvet.vin.repository;

import com.bexchauvet.vin.domain.Wine;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WineRepository extends JpaRepository<Wine, String> {

    @Query("SELECT w FROM Wine w" +
            " WHERE w.color IN :colors AND w.country IN :countries AND w.vintage IN :vintages AND w.averageScore " +
            "BETWEEN :minScore AND :maxScore AND w.currentPrice BETWEEN :minPrice AND :maxPrice")
    List<Wine> getByCriteria(@Param("colors") List<String> colors,
                             @Param("countries") List<String> countries,
                             @Param("vintages") List<Integer> vintages,
                             @Param("minScore") Double minScore,
                             @Param("maxScore") Double maxScore,
                             @Param("minPrice") Double minPrice,
                             @Param("maxPrice") Double maxPrice);

    @Query("SELECT DISTINCT w.country FROM Wine w")
    List<String> getDistinctCountry();

    @Query("SELECT MAX(w.currentPrice) FROM Wine w")
    Double currentMaxPrice();

}
