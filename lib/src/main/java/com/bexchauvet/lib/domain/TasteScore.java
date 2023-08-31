package com.bexchauvet.lib.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;

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
    @JsonBackReference
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User expert;
    @JsonBackReference
    @ManyToOne
    @JoinColumn(name = "wine_id")
    private Wine wine;
    private Instant date;
    private Double score;
    private String commentary;
}
