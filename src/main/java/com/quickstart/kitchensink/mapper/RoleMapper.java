package com.quickstart.kitchensink.mapper;

import com.quickstart.kitchensink.dto.response.RoleDTO;
import com.quickstart.kitchensink.dto.request.role.RoleCreateRequest;
import com.quickstart.kitchensink.model.Permission;
import com.quickstart.kitchensink.model.Role;

import java.util.Collections;
import java.util.Optional;
import java.util.stream.Collectors;

public class RoleMapper {

    public static RoleDTO fromRoleCreateRequest(RoleCreateRequest request) {
        return RoleDTO.of(request.getRoleName(), request.getRoleDescription());
    }

    public static RoleDTO fromEntity(Role role) {
        return RoleDTO.of(role.getId(),
                role.getName(),
                role.getDescription(),
                Optional.ofNullable(role.getPermissions())
                        .orElse(Collections.emptyList())
                        .stream()
                        .map(Permission::getName)
                        .collect(Collectors.toList()));
    }

    public static RoleDTO fromEntityWithoutPermission(Role role) {
        return RoleDTO.of(role.getId(),
                role.getName(),
                role.getDescription());
    }
}
