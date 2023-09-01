package com.bexchauvet.vin.rest;


import com.bexchauvet.vin.VinApplication;
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

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.MOCK,
        classes = VinApplication.class)
@AutoConfigureMockMvc
public class AuthControllerIntegrationTest {

    @Autowired
    MockMvc mvc;


    @Test
    @DisplayName("Test to check authentication with bad credentials")
    void login_badCredentials() throws Exception {
        mvc.perform(MockMvcRequestBuilders.post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON).content("{\"username\": \"jane\", " +
                                "\"password\": \"password\", \"roles\": [\"USER\"]}"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("Test to create and authenticate with good credentials")
    void login_GoodCredentials() throws Exception {
        mvc.perform(MockMvcRequestBuilders.post("/auth")
                        .contentType(MediaType.APPLICATION_JSON).content("{\"username\": \"Bob\", " +
                                "\"password\": \"872Mu58o&F#7Qy398n*3\", \"roles\": [\"USER\"]}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.message").exists());
        mvc.perform(MockMvcRequestBuilders.post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON).content("{\"username\": \"Bob\", " +
                                "\"password\": \"872Mu58o&F#7Qy398n*3\", \"roles\": [\"USER\"]}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").exists());
    }

}
