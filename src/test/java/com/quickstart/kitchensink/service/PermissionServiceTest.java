package com.quickstart.kitchensink.service;

import com.quickstart.kitchensink.model.Permission;
import com.quickstart.kitchensink.repository.PermissionRepository;
import com.quickstart.kitchensink.dto.PermissionDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PermissionServiceTest {

    @Mock
    private PermissionRepository permissionRepository;

    @InjectMocks
    private PermissionService permissionService;

    private Permission testPermission;

    @BeforeEach
    void setUp() {
        testPermission = Permission.builder()
            .id("1")
            .name("TEST_PERMISSION")
            .description("Test Permission Description")
            .build();
    }

    @Test
    void getAllPermissions_Success() {
        when(permissionRepository.findAll()).thenReturn(Arrays.asList(testPermission));

        List<PermissionDTO> permissions = permissionService.getAllPermissions();

        assertFalse(permissions.isEmpty());
        assertEquals(1, permissions.size());
        assertEquals(testPermission.getName(), permissions.get(0).getName());
        assertEquals(testPermission.getDescription(), permissions.get(0).getDescription());
    }

    @Test
    void getAllPermissions_FilterOutAllPermission() {
        Permission allPermission = Permission.builder()
            .id("2")
            .name("ALL")
            .description("All Permissions")
            .build();
        when(permissionRepository.findAll()).thenReturn(Arrays.asList(testPermission, allPermission));

        List<PermissionDTO> permissions = permissionService.getAllPermissions();

        assertEquals(1, permissions.size());
        assertEquals(testPermission.getName(), permissions.get(0).getName());
    }

    @Test
    void validateAndGetPermissions_Success() {
        List<String> permissionNames = Arrays.asList("TEST_PERMISSION");
        when(permissionRepository.findByNameIn(permissionNames)).thenReturn(Arrays.asList(testPermission));

        List<Permission> permissions = permissionService.validateAndGetPermissions(permissionNames);

        assertFalse(permissions.isEmpty());
        assertEquals(1, permissions.size());
        assertEquals(testPermission.getName(), permissions.get(0).getName());
    }

    @Test
    void validateAndGetPermissions_EmptyList() {
        List<String> permissionNames = List.of();

        List<Permission> permissions = permissionService.validateAndGetPermissions(permissionNames);

        assertTrue(permissions.isEmpty());
        verify(permissionRepository, never()).findByNameIn(any());
    }

    @Test
    void validateAndGetPermissions_NoValidPermissions() {
        List<String> permissionNames = Arrays.asList("INVALID_PERMISSION");
        when(permissionRepository.findByNameIn(permissionNames)).thenReturn(List.of());

        assertThrows(IllegalArgumentException.class, () -> 
            permissionService.validateAndGetPermissions(permissionNames));
    }
}
