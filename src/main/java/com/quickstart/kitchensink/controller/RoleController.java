package com.quickstart.kitchensink.controller;

import com.quickstart.kitchensink.dto.response.RoleDTO;
import com.quickstart.kitchensink.dto.request.role.RoleCreateRequest;
import com.quickstart.kitchensink.dto.request.role.RolePermissionUpdateRequest;
import com.quickstart.kitchensink.mapper.RoleMapper;
import com.quickstart.kitchensink.service.RoleService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.management.relation.RoleNotFoundException;
import java.net.URI;

@RestController
@RequiredArgsConstructor
@RequestMapping("/roles")
public class RoleController {

    private final RoleService roleService;

    @GetMapping
    public ResponseEntity<?> getRoles() {
        return ResponseEntity.ok(roleService.getAllRoles());
    }

    @PostMapping
    public ResponseEntity<?> createRole(@Valid @RequestBody RoleCreateRequest request) {
        RoleDTO roleDTO = RoleMapper.fromRoleCreateRequest(request);
        roleDTO = roleService.createRole(roleDTO);
        return ResponseEntity
                .created(URI.create("/api/roles/" + roleDTO.getRoleId()))
                .body(roleDTO);
    }

    @GetMapping("/{roleId}")
    public ResponseEntity<?> getRole(@NotBlank @PathVariable String roleId) throws RoleNotFoundException {
        RoleDTO roleDTO = roleService.getRole(roleId);
        return ResponseEntity.ok(roleDTO);
    }

    @DeleteMapping("/{roleId}")
    public ResponseEntity<?> deleteRole(@NotBlank @PathVariable String roleId) throws RoleNotFoundException {
        roleService.deleteRole(roleId);
        return ResponseEntity.accepted().build();
    }

    @PutMapping("/{roleId}/assign-permissions")
    public ResponseEntity<?> assignPermissionsToRole(@NotBlank @PathVariable String roleId,
                                                     @Valid @RequestBody RolePermissionUpdateRequest request) throws RoleNotFoundException {
        RoleDTO roleDTO = roleService.assignPermissionsToRole(roleId, request.getPermissions());
        return ResponseEntity.ok(roleDTO);
    }
}
