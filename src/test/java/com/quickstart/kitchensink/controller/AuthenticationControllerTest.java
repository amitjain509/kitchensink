package com.quickstart.kitchensink.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.quickstart.kitchensink.application.AuthApplicationService;
import com.quickstart.kitchensink.dto.request.AuthRequestDTO;
import com.quickstart.kitchensink.dto.request.user.PasswordResetRequest;
import com.quickstart.kitchensink.dto.response.AuthResponseDTO;
import com.quickstart.kitchensink.model.Role;
import com.quickstart.kitchensink.model.User;
import com.quickstart.kitchensink.security.jwt.JwtService;
import com.quickstart.kitchensink.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON;

@ExtendWith(MockitoExtension.class)
class AuthenticationControllerTest {
    private MockMvc mockMvc;
    private ObjectMapper objectMapper = new ObjectMapper();

    @Mock
    private UserService userService;

    @Mock
    private JwtService jwtService;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private AuthApplicationService authApplicationService;

    @Mock
    private AuthenticationManager authenticationManager;

    @InjectMocks
    private AuthenticationController authenticationController;

    private User user;

    @BeforeEach
    void setUp() {
        user = User.builder()
                .email("test@example.com")
                .roles(List.of(Role.builder().name("ROLE_USER").build()))
                .build();
        mockMvc = MockMvcBuilders.standaloneSetup(authenticationController).build();
    }

    @Test
    void authenticate_ShouldReturnAuthResponse_WhenCredentialsAreValid() throws Exception {
        AuthRequestDTO request = new AuthRequestDTO("test@example.com", "password");
        String token = "mockToken";

        when(authApplicationService.authenticate(any(AuthRequestDTO.class))).thenReturn(AuthResponseDTO.from(user, token));

        mockMvc.perform(MockMvcRequestBuilders.post("/auth/login")
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.email").value(user.getEmail()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.token").value(token));
    }

    @Test
    void resetPassword_ShouldReturnAuthResponse_WhenOldPasswordIsValid() throws Exception {
        PasswordResetRequest request = new PasswordResetRequest("test@example.com", "oldPassword", "newPassword");
        String token = "mockToken";

        when(authApplicationService.authenticate(any(PasswordResetRequest.class))).thenReturn(AuthResponseDTO.from(user, token));

        mockMvc.perform(MockMvcRequestBuilders.post("/auth/reset-password")
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.email").value(user.getEmail()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.token").value(token));
    }

    @Test
    void resetPassword_ShouldReturn400_WhenEmailIsBlank() throws Exception {
        PasswordResetRequest request = new PasswordResetRequest("", "oldPassword", "newPassword");

        mockMvc.perform(MockMvcRequestBuilders.post("/auth/reset-password")
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    void resetPassword_ShouldReturn400_WhenOldPasswordIsBlank() throws Exception {
        PasswordResetRequest request = new PasswordResetRequest("test@example.com", "", "newPassword");

        mockMvc.perform(MockMvcRequestBuilders.post("/auth/reset-password")
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    void resetPassword_ShouldReturn400_WhenNewPasswordIsBlank() throws Exception {
        PasswordResetRequest request = new PasswordResetRequest("test@example.com", "oldPassword", "");

        mockMvc.perform(MockMvcRequestBuilders.post("/auth/reset-password")
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    void resetPassword_ShouldReturn400_WhenNewPasswordExceedsMaxLength() throws Exception {
        PasswordResetRequest request = new PasswordResetRequest("test@example.com", "oldPassword", "1234567890123456");

        mockMvc.perform(MockMvcRequestBuilders.post("/auth/reset-password")
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }
}
