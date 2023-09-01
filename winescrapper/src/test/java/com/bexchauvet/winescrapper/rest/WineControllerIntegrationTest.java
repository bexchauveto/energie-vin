package com.bexchauvet.winescrapper.rest;


import com.bexchauvet.winescrapper.WineScrapperApplication;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.MOCK,
        classes = WineScrapperApplication.class)
@AutoConfigureMockMvc
public class WineControllerIntegrationTest {

    @Autowired
    MockMvc mvc;


    @Test
    @DisplayName("Test sending wineMQ object to RabbitMQ via rest API")
    @Disabled
    void testWineCreation() throws Exception {
        mvc.perform(MockMvcRequestBuilders.post("/wines")
                        .contentType(MediaType.APPLICATION_JSON).content("{\"name\": \"Wine name\", " +
                                "\"producer\": \"producer\", \"vintage\": 2015," +
                                "\"color\": \"ROUGE\", \"country\": \"FRANCE\", " +
                                "\"url\": \"url\", \"date\": \"2023-08-31T10:15:19.027Z\"," +
                                "\"price\": 10.2}"))
                .andExpect(status().isOk());
        /*assertThat(this.config.winesIn, equalTo("{\"name\": \"Wine name\", " +
                "\"producer\": \"producer\", \"vintage\": 2015," +
                "\"color\": \"ROUGE\", \"country\": \"FRANCE\", " +
                "\"url\": \"url\", \"date\": \"2023-08-31T10:15:19.027Z\"," +
                "\"price\": 10.2}"));*/
    }

}
