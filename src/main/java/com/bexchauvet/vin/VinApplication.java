package com.bexchauvet.vin;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableAutoConfiguration(exclude = SecurityAutoConfiguration.class)
@EnableScheduling
@EntityScan(basePackages = {"com.bexchauvet.vin.domain"})
public class VinApplication {

    public static void main(String[] args) {
        SpringApplication.run(VinApplication.class, args);
    }

}
