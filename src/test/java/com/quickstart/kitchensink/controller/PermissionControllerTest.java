package com.quickstart.kitchensink.controller;

import com.quickstart.kitchensink.dto.PermissionDTO;
import com.quickstart.kitchensink.service.PermissionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;
import java.util.Collections;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PermissionControllerTest {
    private MockMvc mockMvc;

    @Mock
    private PermissionService permissionService;

    @InjectMocks
    private PermissionController permissionController;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(permissionController).build();
    }

    @Test
    void getPermissions_ShouldReturnListOfPermissions_WhenPermissionsExist() throws Exception {
        List<PermissionDTO> permissions = List.of(
                new PermissionDTO("1", "READ", "Read permission"),
                new PermissionDTO("2", "WRITE", "Write permission")
        );

        when(permissionService.getAllPermissions()).thenReturn(permissions);

        mockMvc.perform(MockMvcRequestBuilders.get("/permissions")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.length()").value(2))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].id").value("1"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].name").value("READ"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].description").value("Read permission"));

        verify(permissionService, times(1)).getAllPermissions();
    }

    @Test
    void getPermissions_ShouldReturnEmptyList_WhenNoPermissionsExist() throws Exception {
        when(permissionService.getAllPermissions()).thenReturn(Collections.emptyList());

        mockMvc.perform(MockMvcRequestBuilders.get("/permissions")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.length()").value(0));

        verify(permissionService, times(1)).getAllPermissions();
    }
}