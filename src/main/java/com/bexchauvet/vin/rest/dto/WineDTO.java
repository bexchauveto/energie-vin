package com.bexchauvet.vin.rest.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.List;
import java.util.Map;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class WineDTO {
    private String name;
    private String producer;
    private Integer vintage;
    private String color;
    private String country;
    @JsonProperty("average_score")
    private Double averageScore;
    @JsonProperty("expert_commentary")
    private List<String> expertCommentary;
    @JsonProperty("current_price")
    private Double currentPrice;
    @JsonProperty("past_price")
    private Map<Instant, Double> pastPrice;
}
