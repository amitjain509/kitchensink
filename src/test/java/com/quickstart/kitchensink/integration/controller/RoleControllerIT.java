package com.quickstart.kitchensink.integration.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.quickstart.kitchensink.dto.PermissionDTO;
import com.quickstart.kitchensink.dto.request.role.RoleCreateRequest;
import com.quickstart.kitchensink.dto.request.role.RolePermissionUpdateRequest;
import com.quickstart.kitchensink.dto.response.RoleDTO;
import com.quickstart.kitchensink.integration.AbstractBaseIntegrationTest;
import com.quickstart.kitchensink.repository.RoleRepository;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.List;
import java.util.stream.Collectors;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith(SpringExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class RoleControllerIT extends AbstractBaseIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private ObjectMapper objectMapper;

    private static String roleId;

    @Test
    @Order(1)
    void shouldCreateRole() throws Exception {
        RoleCreateRequest request = new RoleCreateRequest("ROLE_MANAGER", "Manager role");

        String response = mockMvc.perform(post("/roles")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + jwtToken)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.roleName", is("ROLE_MANAGER")))
                .andExpect(jsonPath("$.roleDescription", is("Manager role")))
                .andReturn().getResponse().getContentAsString();

        RoleDTO createdRole = objectMapper.readValue(response, RoleDTO.class);
        roleId = createdRole.getRoleId();
        Assertions.assertNotNull(roleId, "Role ID should not be null");
    }

    @Test
    void shouldGetAllRoles() throws Exception {
        mockMvc.perform(get("/roles")
                        .header("Authorization", "Bearer " + jwtToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", not(empty())));
    }

    @Test
    void shouldGetRoleById() throws Exception {
        mockMvc.perform(get("/roles/" + roleId)
                        .header("Authorization", "Bearer " + jwtToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.roleId", is(roleId)))
                .andExpect(jsonPath("$.roleName", is("ROLE_MANAGER")));
    }

    @Test
    void shouldAssignPermissionsToRole() throws Exception {
        List<String> permissions = List.of("USER_CREATE", "USER_DELETE");
        RolePermissionUpdateRequest request = new RolePermissionUpdateRequest(roleId, List.of("USER_CREATE", "USER_DELETE"));

        MvcResult mvcResult = mockMvc.perform(put("/roles/" + roleId + "/assign-permissions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + jwtToken)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk()).andReturn();
        RoleDTO roleDTO = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), RoleDTO.class);
        Assertions.assertEquals(roleDTO.getPermissions().stream().map(PermissionDTO::getName).collect(Collectors.toList()), permissions);
    }
}
