package com.quickstart.kitchensink.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.quickstart.kitchensink.dto.request.role.RoleCreateRequest;
import com.quickstart.kitchensink.dto.request.role.RolePermissionUpdateRequest;
import com.quickstart.kitchensink.dto.response.RoleDTO;
import com.quickstart.kitchensink.exception.ApplicationErrorCode;
import com.quickstart.kitchensink.exception.GlobalExceptionHandler;
import com.quickstart.kitchensink.exception.KitchenSinkException;
import com.quickstart.kitchensink.model.Permission;
import com.quickstart.kitchensink.service.RoleService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import javax.management.relation.RoleNotFoundException;
import java.util.Collections;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class RoleControllerTest {

    private MockMvc mockMvc;
    private ObjectMapper objectMapper = new ObjectMapper();

    @Mock
    private RoleService roleService;

    @InjectMocks
    private RoleController roleController;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(roleController)
                .setControllerAdvice(GlobalExceptionHandler.class)
                .build();
    }

    @Test
    void getRoles_ShouldReturnListOfRoles() throws Exception {
        when(roleService.getAllRoles()).thenReturn(Collections.singletonList(getDummyRole()));

        mockMvc.perform(MockMvcRequestBuilders.get("/roles"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
    }

    @Test
    void createRole_ShouldReturnCreatedRole() throws Exception {
        RoleCreateRequest request = new RoleCreateRequest("ADMIN", "");
        RoleDTO response = getDummyRole();

        when(roleService.createRole(any())).thenReturn(response);

        mockMvc.perform(MockMvcRequestBuilders.post("/roles")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.roleId").value("1"))
                .andExpect(jsonPath("$.roleName").value("ADMIN"));
    }

    @Test
    void createRole_ShouldReturn400_WhenRoleNameIsBlank() throws Exception {
        RoleCreateRequest request = new RoleCreateRequest("", "");

        mockMvc.perform(MockMvcRequestBuilders.post("/roles")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void getRole_ShouldReturnRole_WhenRoleExists() throws Exception {
        RoleDTO roleDTO = getDummyRole();
        when(roleService.getRole("1")).thenReturn(roleDTO);

        mockMvc.perform(MockMvcRequestBuilders.get("/roles/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.roleId").value("1"))
                .andExpect(jsonPath("$.roleName").value("ADMIN"));
    }

    @Test
    void getRole_ShouldReturn404_WhenRoleDoesNotExist() throws Exception {
        when(roleService.getRole("99")).thenThrow(KitchenSinkException
                .builder(ApplicationErrorCode.ROLE_NOT_FOUND)
                .referenceId("99")
                .build());

        mockMvc.perform(MockMvcRequestBuilders.get("/roles/99"))
                .andExpect(status().isNotFound());
    }

    @Test
    void deleteRole_ShouldReturnAccepted_WhenRoleIsDeleted() throws Exception {
        doNothing().when(roleService).deleteRole("1");

        mockMvc.perform(MockMvcRequestBuilders.delete("/roles/1"))
                .andExpect(status().isAccepted());
    }

    @Test
    void deleteRole_ShouldReturn404_WhenRoleDoesNotExist() throws Exception {
        doThrow(KitchenSinkException
                .builder(ApplicationErrorCode.ROLE_NOT_FOUND)
                .referenceId("99")
                .build()).when(roleService).deleteRole("99");

        mockMvc.perform(MockMvcRequestBuilders.delete("/roles/99"))
                .andExpect(status().isNotFound());
    }

    @Test
    void assignPermissionsToRole_ShouldReturnUpdatedRole_WhenPermissionsAreAssigned() throws Exception {
        RolePermissionUpdateRequest request = new RolePermissionUpdateRequest("1", Collections.singletonList("USER_CREATE"));
        RoleDTO response = RoleDTO.of("1", "ADMIN", "", Collections.singletonList(Permission.of("USER_CREATE", "")));

        when(roleService.assignPermissionsToRole(eq("1"), any())).thenReturn(response);

        mockMvc.perform(MockMvcRequestBuilders.put("/roles/1/assign-permissions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.permissions[0].name").value("USER_CREATE"));
    }

    @Test
    void assignPermissionsToRole_ShouldReturn400_WhenRequestIsInvalid() throws Exception {
        RolePermissionUpdateRequest request = new RolePermissionUpdateRequest("", Collections.emptyList());

        mockMvc.perform(MockMvcRequestBuilders.put("/roles/1/assign-permissions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    RoleDTO getDummyRole() {
        return RoleDTO.of("1", "ADMIN", "", Collections.emptyList());
    }
}
