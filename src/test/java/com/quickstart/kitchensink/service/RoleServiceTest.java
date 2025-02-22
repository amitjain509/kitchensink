package com.quickstart.kitchensink.service;

import com.quickstart.kitchensink.dto.response.BasicRoleDTO;
import com.quickstart.kitchensink.dto.response.RoleDTO;
import com.quickstart.kitchensink.exception.KitchenSinkException;
import com.quickstart.kitchensink.model.Permission;
import com.quickstart.kitchensink.model.Role;
import com.quickstart.kitchensink.repository.RoleRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RoleServiceTest {

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private PermissionService permissionService;

    @InjectMocks
    private RoleService roleService;

    private Role role;
    private Role adminRole;
    private BasicRoleDTO roleDTO;

    @BeforeEach
    void setUp() {
        role = Role.of("USER", "User role");
        adminRole = Role.of("ADMIN", "ADMIN role");
        roleDTO = BasicRoleDTO.of("USER", "User role");
    }

    @Test
    void createRole_ShouldCreateRole_WhenRoleDoesNotExist() {
        when(roleRepository.existsByName(roleDTO.getRoleName())).thenReturn(false);
        when(roleRepository.save(any(Role.class))).thenReturn(role);

        BasicRoleDTO createdRole = roleService.createRole(roleDTO);

        assertNotNull(createdRole);
        assertEquals(roleDTO.getRoleName(), createdRole.getRoleName());
        verify(roleRepository, times(1)).save(any(Role.class));
    }

    @Test
    void createRole_ShouldThrowException_WhenRoleAlreadyExists() {
        when(roleRepository.existsByName(roleDTO.getRoleName())).thenReturn(true);

        assertThrows(KitchenSinkException.class, () -> roleService.createRole(roleDTO));
    }

    @Test
    void deleteRole_ShouldDeleteRole_WhenRoleExists() {
        when(roleRepository.existsById("1")).thenReturn(true);

        roleService.deleteRole("1");

        verify(roleRepository, times(1)).deleteById("1");
    }

    @Test
    void deleteRole_ShouldThrowException_WhenRoleDoesNotExist() {
        when(roleRepository.existsById("1")).thenReturn(false);

        assertThrows(KitchenSinkException.class, () -> roleService.deleteRole("1"));
    }

    @Test
    void getRole_ShouldReturnRole_WhenRoleExists() {
        when(roleRepository.findById("1")).thenReturn(Optional.of(role));
        RoleDTO foundRole = roleService.getRole("1");

        assertNotNull(foundRole);
        assertEquals("USER", foundRole.getRoleName());
    }

    @Test
    void getRole_ShouldThrowException_WhenRoleDoesNotExist() {
        when(roleRepository.findById("1")).thenReturn(Optional.empty());

        assertThrows(KitchenSinkException.class, () -> roleService.getRole("1"));
    }

    @Test
    void getAllRoles_ShouldReturnRoles_WithoutAdmin() {
        when(roleRepository.findAll()).thenReturn(List.of(role, adminRole));
        List<BasicRoleDTO> roleDTOS = roleService.getAllRoles();

        Assertions.assertEquals(roleDTOS.size(), 1);
        Assertions.assertEquals(roleDTOS.get(0).getRoleName(), "USER");
    }

    @Test
    void getAllRoles_ShouldReturnNoRole_WithoutAdmin() {
        when(roleRepository.findAll()).thenReturn(List.of(adminRole));
        List<BasicRoleDTO> roleDTOS = roleService.getAllRoles();

        Assertions.assertEquals(roleDTOS.size(), 0);
    }

    @Test
    void getRoles_ShouldReturnRole_WhenRolesValid() {
        List<String> roleList = List.of("USER");
        when(roleRepository.findByNameIn(roleList)).thenReturn(List.of(role));
        List<Role> roles = roleService.validateAndGetRoles(roleList);

        Assertions.assertEquals(roles.size(), 1);
    }

    @Test
    void getRoles_ShouldThrowException_WhenRoleDoesNotExist() {
        List<String> roleList = List.of("USER");
        when(roleRepository.findByNameIn(roleList)).thenReturn(List.of());
        assertThrows(KitchenSinkException.class, () -> roleService.validateAndGetRoles(roleList));
    }

    @Test
    void assignPermissionsToRole_ShouldAssignPermissions_WhenRoleExists() {
        List<Permission> permissions = List.of(new Permission("1", "USER_CREATE", "Can create users"));
        when(roleRepository.findById("1")).thenReturn(Optional.of(role));
        when(roleRepository.save(role)).thenReturn(role);

        RoleDTO updatedRole = roleService.assignPermissionsToRole("1", permissions);

        assertNotNull(updatedRole);
        verify(roleRepository, times(1)).save(role);
    }

    @Test
    void assignPermissionsToRole_ShouldThrowException_WhenRoleDoesNotExist() {
        List<Permission> permissions = List.of(new Permission("1", "USER_CREATE", "Can create users"));
        when(roleRepository.findById("1")).thenReturn(Optional.empty());

        assertThrows(KitchenSinkException.class, () -> roleService.assignPermissionsToRole("1", permissions));
    }
}
