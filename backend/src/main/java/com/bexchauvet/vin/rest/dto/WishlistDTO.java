package com.bexchauvet.vin.rest.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class WishlistDTO {
    private String name;
    private List<String> colors;
    private List<String> countries;
    private List<Integer> vintages;
    @JsonProperty("min_score")
    private Double minScore;
    @JsonProperty("max_score")
    private Double maxScore;
    @JsonProperty("min_price")
    private Double minPrice;
    @JsonProperty("max_price")
    private Double maxPrice;
}
