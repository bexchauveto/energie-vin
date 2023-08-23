package com.bexchauvet.vin.error.dto;

import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class ErrorDTO {
    private String message;
    private List<String> errors;

}