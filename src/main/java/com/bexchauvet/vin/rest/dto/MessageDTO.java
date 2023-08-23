package com.bexchauvet.vin.rest.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class MessageDTO {
    private String message;
    private Object data;
}