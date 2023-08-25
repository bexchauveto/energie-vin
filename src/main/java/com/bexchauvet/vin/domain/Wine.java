package com.bexchauvet.vin.domain;


import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Entity
@Table(name = "wines")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Wine {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "wine_id")
    private Long wineId;
    private String name;
    private String producer;
    private Integer vintage;
    private String color;
    private String country;
    @JsonManagedReference
    @Column(name = "taste_score")
    @OneToMany(mappedBy = "wine", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<TasteScore> tasteScores;
    @Column(name = "average_score")
    private Double averageScore;
    @JsonManagedReference
    @OneToMany(mappedBy = "wine", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Price> prices;
    @Column(name = "current_price")
    private Double currentPrice;

    public List<String> expertCommentary() {
        return this.tasteScores.stream().map(TasteScore::getCommentary).collect(Collectors.toList());
    }

    public Map<Instant, Double> pastPrices() {
        Collections.sort(prices);
        Map<Instant, Double> pastPrices = new HashMap<>();
        for (Price price : prices) {
            pastPrices.put(price.getDate(), price.getPrice());
        }
        return pastPrices;
    }


}
