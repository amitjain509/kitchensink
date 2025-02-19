package com.quickstart.kitchensink.integration.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.quickstart.kitchensink.dto.request.user.PasswordResetRequest;
import com.quickstart.kitchensink.dto.response.AuthResponseDTO;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS) // Ensure lifecycle is per class
public class ZTestFinalizer {

    @Autowired
    private MockMvc mockMvc;

    @AfterAll
    @Order(999) // Ensure it runs last
    static void resetSystem(@Autowired MockMvc mockMvc, @Autowired ObjectMapper objectMapper) throws Exception {
        PasswordResetRequest resetRequest = new PasswordResetRequest("admin@example.com", "admin@123", "admin@124");

        MvcResult result = mockMvc.perform(post("/auth/reset-password")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(resetRequest)))
                .andExpect(status().isOk())
                .andReturn();

        String responseBody = result.getResponse().getContentAsString();
        AuthResponseDTO response = objectMapper.readValue(responseBody, AuthResponseDTO.class);

        assertThat(response.getToken()).isNotNull();
    }
}
