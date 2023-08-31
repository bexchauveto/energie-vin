package com.bexchauvet.vin.rest.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.time.Instant;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class TasteScoreDTO {

    @JsonProperty("wine_id")
    private String wineID;
    private Instant date;
    private Double score;
    private String commentary;

}
