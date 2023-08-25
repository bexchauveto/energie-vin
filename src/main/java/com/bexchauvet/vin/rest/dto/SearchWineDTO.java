package com.bexchauvet.vin.rest.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class SearchWineDTO {

    protected List<String> colors;
    protected List<String> countries;
    protected List<Integer> vintages;
    @JsonProperty("min_score")
    protected Double minScore;
    @JsonProperty("max_score")
    protected Double maxScore;
    @JsonProperty("min_price")
    protected Double minPrice;
    @JsonProperty("max_price")
    protected Double maxPrice;

}
