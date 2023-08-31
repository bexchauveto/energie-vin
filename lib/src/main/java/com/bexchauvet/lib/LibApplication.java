package com.bexchauvet.lib;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EntityScan(basePackages = {"com.bexchauvet.lib.domain"})
@EnableJpaRepositories(basePackages = {"com.bexchauvet.lib.repository"})
public class LibApplication {
    public static void main(String[] args) {
        SpringApplication.run(LibApplication.class, args);
    }
}