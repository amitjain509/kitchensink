package com.quickstart.kitchensink.controller;

import com.quickstart.kitchensink.application.RoleApplicationService;
import com.quickstart.kitchensink.dto.request.role.RoleCreateRequest;
import com.quickstart.kitchensink.dto.request.role.RolePermissionUpdateRequest;
import com.quickstart.kitchensink.dto.response.RoleDTO;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequiredArgsConstructor
@RequestMapping("/roles")
public class RoleController {
    private final RoleApplicationService roleApplicationService;

    @GetMapping
    @PreAuthorize("hasAnyAuthority('ROLE_VIEW')")
    public ResponseEntity<?> getRoles() {
        return ResponseEntity.ok(roleApplicationService.getAllRoles());
    }

    @PostMapping
    @PreAuthorize("hasAnyAuthority('ROLE_CREATE')")
    public ResponseEntity<?> createRole(@Valid @RequestBody RoleCreateRequest request) {
        RoleDTO roleDTO = roleApplicationService.createRole(request);
        return ResponseEntity
                .created(URI.create("/api/roles/" + roleDTO.getRoleId()))
                .body(roleDTO);
    }

    @GetMapping("/{roleId}")
    @PreAuthorize("hasAnyAuthority('ROLE_VIEW')")
    public ResponseEntity<?> getRole(@NotBlank @PathVariable String roleId) {
        RoleDTO roleDTO = roleApplicationService.getRole(roleId);
        return ResponseEntity.ok(roleDTO);
    }

    @DeleteMapping("/{roleId}")
    @PreAuthorize("hasAnyAuthority('ROLE_DELETE')")
    public ResponseEntity<?> deleteRole(@NotBlank @PathVariable String roleId) {
        roleApplicationService.deleteRole(roleId);
        return ResponseEntity.accepted().build();
    }

    @PutMapping("/{roleId}/assign-permissions")
    @PreAuthorize("hasAnyAuthority('ROLE_EDIT')")
    public ResponseEntity<?> assignPermissionsToRole(@NotBlank @PathVariable String roleId,
                                                     @Valid @RequestBody RolePermissionUpdateRequest request) {
        RoleDTO roleDTO = roleApplicationService.assignPermissionsToRole(roleId, request);
        return ResponseEntity.ok(roleDTO);
    }
}
