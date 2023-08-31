package com.bexchauvet.winescrapper.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
@OpenAPIDefinition(
        info = @Info(title = "Wine scrapper API", version = "1.0",
                contact = @Contact(name = "Olivier Bex-Chauvet", email = "olivier@bex-chauvet.fr",
                        url = "https://bex-chauvet.fr"),
                description = "This API is mock a wine scrapper microservices"))
public class OpenAPIConfig {


    @Value("${server-swagger}")
    private String serverUrl;

    @Bean
    public OpenAPI customizeOpenAPI() {
        return new OpenAPI().servers(List.of(new Server().url("http://localhost:8786")
                //.url(serverUrl)
                .description(serverUrl.contains("localhost") ? "Development" : "Production")));
    }

}
