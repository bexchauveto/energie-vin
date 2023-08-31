package com.bexchauvet.winescrapper;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication//(exclude = {DataSourceAutoConfiguration.class })
public class WineScrapperApplication {

	public static void main(String[] args) {
		SpringApplication.run(WineScrapperApplication.class, args);
	}

}
