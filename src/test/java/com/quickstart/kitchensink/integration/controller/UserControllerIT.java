package com.quickstart.kitchensink.integration.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.quickstart.kitchensink.dto.request.user.UserCreateRequest;
import com.quickstart.kitchensink.dto.request.user.UserUpdateRequest;
import com.quickstart.kitchensink.enums.UserType;
import com.quickstart.kitchensink.integration.AbstractBaseIntegrationTest;
import com.quickstart.kitchensink.model.User;
import com.quickstart.kitchensink.repository.UserRepository;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith(SpringExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class UserControllerIT extends AbstractBaseIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ObjectMapper objectMapper;
    String email = "john.doe@example.com";
    private static String userId;

    @Test
    @Order(1)
    void shouldCreateUser() throws Exception {
        UserCreateRequest request = new UserCreateRequest("John Doe", email, "9945242509",
                "password123", UserType.USER);

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("authorization", "Bearer " + jwtToken)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.email", is("john.doe@example.com")));

        Optional<User> savedUser = userRepository.findByEmail("john.doe@example.com");
        assert savedUser.isPresent();
        userId = savedUser.get().getId();
    }

    @Test
    void shouldGetUserById() throws Exception {
        mockMvc.perform(get("/users/" + email)
                        .header("authorization", "Bearer " + jwtToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email", is("john.doe@example.com")));
    }

    @Test
    void shouldUpdateUser() throws Exception {
        UserUpdateRequest updateRequest = new UserUpdateRequest("9988776655", "John Updated", email, UserType.USER);

        mockMvc.perform(put("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + jwtToken)
                        .content(objectMapper.writeValueAsString(updateRequest)))
                .andExpect(status().isNoContent());
        Assertions.assertEquals(userRepository.findByEmail(email).get().getPhoneNumber(), "9988776655");
    }

    @Test
    void shouldLockUser() throws Exception {
        {
            mockMvc.perform(patch("/users/" + userId + "/lock")
                            .header("Authorization", "Bearer " + jwtToken))
                    .andExpect(status().isNoContent());
            Assertions.assertTrue(userRepository.findByEmail(email).get().isLocked());
        }

        {
            mockMvc.perform(patch("/users/" + userId + "/unlock")
                            .header("Authorization", "Bearer " + jwtToken))
                    .andExpect(status().isNoContent());
            Assertions.assertTrue(userRepository.findByEmail(email).get().isAccountNonLocked());
        }
    }

    @Test
    void shouldAssignRolesToUser() throws Exception {
        List<String> roles = List.of("DEFAULT");

        mockMvc.perform(patch("/users/" + userId + "/roles")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + jwtToken)
                        .content(objectMapper.writeValueAsString(roles)))
                .andExpect(status().isNoContent());
        User user = userRepository.findByEmail(email).get();
        Assertions.assertTrue(user.getRoles().get(0).getName().equals("DEFAULT"));
    }

    @Test
    void shouldFindAllUsersByUserType() throws Exception {
        mockMvc.perform(get("/users/userType/" + UserType.USER)
                        .header("Authorization", "Bearer " + jwtToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1)); // Assuming one user exists
    }

//    @Test
//    @Order(100)
//    void shouldDeleteUser() throws Exception {
//        mockMvc.perform(delete("/users/" + userId)
//                        .header("Authorization", "Bearer " + jwtToken))
//                .andExpect(status().isAccepted());
//
//        Optional<User> deletedUser = userRepository.findById(userId);
//        Assertions.assertFalse(deletedUser.isPresent(), "User should be deleted");
//    }
}
