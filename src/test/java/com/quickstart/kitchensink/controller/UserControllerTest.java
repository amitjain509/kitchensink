package com.quickstart.kitchensink.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.quickstart.kitchensink.dto.request.user.UserCreateRequest;
import com.quickstart.kitchensink.dto.request.user.UserUpdateRequest;
import com.quickstart.kitchensink.dto.response.UserDTO;
import com.quickstart.kitchensink.enums.UserType;
import com.quickstart.kitchensink.exception.GlobalExceptionHandler;
import com.quickstart.kitchensink.mapper.UserMapper;
import com.quickstart.kitchensink.service.UserService;
import com.quickstart.kitchensink.validators.UniqueEmailValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserControllerTest {
    private MockMvc mockMvc;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Mock
    private UserService userService;
    @Mock
    private UserMapper userMapper;

    @InjectMocks
    private UniqueEmailValidator uniqueEmailValidator;

    @InjectMocks
    private UserController userController;

    private UserDTO userDTO;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(userController)
                .setControllerAdvice(GlobalExceptionHandler.class)
                .build();
        userDTO = UserDTO.builder()
                .userId("1")
                .email("test@example.com")
                .userType(UserType.USER)
                .build();

        uniqueEmailValidator = Mockito.spy(new UniqueEmailValidator(userService));

        lenient().when(uniqueEmailValidator.isValid(anyString(), any())).thenReturn(true);
    }

    @Test
    @Disabled
    void createUser_ShouldReturn201_WhenRequestIsValid() throws Exception {
        UserCreateRequest request = new UserCreateRequest("test@example.com", "password", "9945242509", "1", UserType.USER);
        when(userService.createUser(any())).thenReturn(userDTO);

        mockMvc.perform(MockMvcRequestBuilders.post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.userId").value(userDTO.getUserId()));
    }

    @Test
    @Disabled
    void createUser_ShouldReturn400_WhenEmailIsInvalid() throws Exception {
        UserCreateRequest request = new UserCreateRequest("name", "", "9945242509", "1", UserType.USER);
        when(userService.createUser(userMapper.fromCreateRequest(request))).thenReturn(userDTO);
        mockMvc.perform(MockMvcRequestBuilders.post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    void getUserByEmail_ShouldReturn404_WhenUserDoesNotExist() throws Exception {
        when(userService.getUserByEmail(anyString())).thenThrow(new UsernameNotFoundException("User does not exists"));

        mockMvc.perform(MockMvcRequestBuilders.get("/users/{email}", "unknown@example.com"))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    void deleteUser_ShouldReturn202_WhenUserExists() throws Exception {
        doNothing().when(userService).deleteUser(anyString());

        mockMvc.perform(MockMvcRequestBuilders.delete("/users/{userId}", "1"))
                .andExpect(MockMvcResultMatchers.status().isAccepted());
    }

    @Test
    void updateUser_ShouldReturn204_WhenRequestIsValid() throws Exception {
        UserUpdateRequest request = new UserUpdateRequest("9945242509", "Test", "new@example.com", "", UserType.USER);
        doNothing().when(userService).updateUser(any());

        mockMvc.perform(MockMvcRequestBuilders.put("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(MockMvcResultMatchers.status().isNoContent());
    }

    @Test
    void findAllUsers_ShouldReturnUsers_WhenUsersExist() throws Exception {
        when(userService.findAllUsersByUserType(UserType.USER)).thenReturn(List.of(userDTO));

        mockMvc.perform(MockMvcRequestBuilders.get("/users/userType/{userType}", UserType.USER))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.length()").value(1));
    }

    @Test
    void findAllUsers_ShouldReturnEmptyList_WhenNoUsersExist() throws Exception {
        when(userService.findAllUsersByUserType(UserType.ADMIN)).thenReturn(Collections.emptyList());

        mockMvc.perform(MockMvcRequestBuilders.get("/users/userType/{userType}", UserType.ADMIN))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.length()").value(0));
    }
}
