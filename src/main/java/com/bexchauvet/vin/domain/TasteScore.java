package com.bexchauvet.vin.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "scores")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TasteScore {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "taste_score_id")
    private Long tasteScoreId;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User expert;
    @ManyToOne
    @JoinColumn(name = "wine_id")
    private Wine wine;
    private Double score;
    private String commentary;
}
