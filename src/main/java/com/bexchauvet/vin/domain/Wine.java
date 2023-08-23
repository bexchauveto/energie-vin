package com.bexchauvet.vin.domain;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Collections;
import java.util.List;

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
    @Column(name = "taste_score")
    @OneToMany(mappedBy = "wines", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<TasteScore> tasteScores;
    @OneToMany(mappedBy = "wines", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Price> prices;

    public Double averageScore() {
        Double score = 0.0;
        for (TasteScore tasteScore : this.tasteScores) {
            score += tasteScore.getScore();
        }
        return score / tasteScores.size();
    }

    public Double latestPrice() {
        Collections.sort(prices);
        return prices.get(0).getPrice();
    }


}
