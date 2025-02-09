package com.quickstart.kitchensink.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.quickstart.kitchensink.dto.MemberDTO;
import com.quickstart.kitchensink.model.Member;
import com.quickstart.kitchensink.repository.MemberRepository;
import com.quickstart.kitchensink.request.MemberRequest;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Testcontainers
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class MemberControllerIT {
    @Container
    static MongoDBContainer mongoDBContainer = new MongoDBContainer(DockerImageName.parse("mongo:6.0"))
            .withExposedPorts(27017);

    @BeforeAll
    static void setupMongoDB() {
        System.setProperty("spring.data.mongodb.uri", mongoDBContainer.getReplicaSetUrl());
    }

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private ObjectMapper objectMapper;


    @Test
    @Order(1)
    void shouldReturnAllMembers() throws Exception {
        MemberRequest memberRequest = new MemberRequest("+919812345678", "John Doe", "john.doe@example.com");

        mockMvc.perform(post("/members")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(memberRequest)))
                .andExpect(status().isOk());

        mockMvc.perform(get("/members"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("John Doe"));
    }

    @Test
    @Order(2)
    void shouldCreateNewMember() throws Exception {
        MemberRequest memberRequest = new MemberRequest("+919812345678", "Jane Doe", "jane.doe@example.com");

        mockMvc.perform(post("/members")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(memberRequest)))
                .andExpect(status().isOk());

        List<Member> members = memberRepository.findAll();
        assertThat(members).hasSize(2);
    }

    @Test
    @Order(3)
    void shouldReturnMemberById() throws Exception {
        List<Member> members = memberRepository.findAll();
        mockMvc.perform(get("/members/{id}", members.get(0).getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("John Doe"));
    }

    @Test
    @Order(4)
    void shouldReturnNotFoundForInvalidMemberId() throws Exception {
        mockMvc.perform(get("/members/{id}", "invalid-id"))
                .andExpect(status().isNotFound());
    }

    @Test
    @Order(5)
    void shouldReturnBadRequestForInvalidMemberRequest() throws Exception {
        MemberRequest invalidRequest = new MemberRequest("", "", "invalid-email");

        mockMvc.perform(post("/members")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @Order(6)
    void shouldHandleDuplicateEmailException() throws Exception {
        MemberRequest duplicateMember = new MemberRequest("+919876543210", "John Doe", "john.doe@example.com");

        mockMvc.perform(post("/members")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(duplicateMember)))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.email").value("Email 'john.doe@example.com' is already in use"));
    }

    @Test
    void shouldReturnNoContentWhenNoMembersExist() throws Exception {
        memberRepository.deleteAll();
        mockMvc.perform(get("/members"))
                .andExpect(status().isNoContent());
    }
}
