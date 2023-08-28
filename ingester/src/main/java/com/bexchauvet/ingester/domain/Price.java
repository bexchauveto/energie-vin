package com.bexchauvet.ingester.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;

@Entity
@Table(name = "prices")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Price implements Comparable<Price> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "price_id")
    private Long priceId;
    @JsonBackReference
    @ManyToOne
    @JoinColumn(name = "wine_id")
    private Wine wine;
    private String url;
    private Instant date;
    private Double price;

    @Override
    public int compareTo(Price o) {
        if (this.getDate().isAfter(o.getDate())) {
            return 1;
        } else if (this.getDate().isBefore(o.getDate())) {
            return -1;
        } else {
            return 0;
        }
    }
}

