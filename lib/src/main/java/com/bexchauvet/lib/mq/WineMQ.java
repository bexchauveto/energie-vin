package com.bexchauvet.lib.mq;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class WineMQ {
    private String name;
    private String producer;
    private Integer vintage;
    private String color;
    private String country;
    private String url;
    private Instant date;
    private Double price;
}
