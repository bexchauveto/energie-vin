package com.bexchauvet.lib.repository;

import com.bexchauvet.lib.domain.Wine;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

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
    @Query("SELECT w FROM Wine w WHERE w.name = :name")
    Optional<Wine> findByName(@Param("name") String name);

}
