package com.bexchauvet.ingester.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Table(name = "wishlists")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class WishList {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "wishlist_id")
    private Long wishlistId;
    @JsonBackReference
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
    private String name;
    private List<String> colors;
    private List<String> countries;
    private List<Integer> vintages;
    @Column(name = "min_score")
    private Double minScore;
    @Column(name = "max_score")
    private Double maxScore;
    @Column(name = "min_price")
    private Double minPrice;
    @Column(name = "max_price")
    private Double maxPrice;


}
