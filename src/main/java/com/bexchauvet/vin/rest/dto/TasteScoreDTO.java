package com.bexchauvet.vin.rest.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class TasteScoreDTO {

    @JsonProperty("wine_id")
    private String wineID;
    private Instant date;
    private Double Score;
    private String commentary;

}
