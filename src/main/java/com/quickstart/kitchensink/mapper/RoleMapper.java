package com.quickstart.kitchensink.mapper;

import com.quickstart.kitchensink.dto.request.role.RoleCreateRequest;
import com.quickstart.kitchensink.dto.response.RoleDTO;
import com.quickstart.kitchensink.model.Role;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Objects;
import java.util.Optional;

public class RoleMapper {

    public static RoleDTO fromRoleCreateRequest(RoleCreateRequest request) {
        return RoleDTO.of(request.getRoleName(), request.getRoleDescription());
    }

    public static RoleDTO fromEntity(Role role) {
        return RoleDTO.of(role.getId(),
                role.getName(),
                role.getDescription(),
                new ArrayList<>(Optional.ofNullable(role.getPermissions())
                        .orElse(Collections.emptyList())));
    }

    public static RoleDTO fromEntityWithoutPermission(Role role) {
        if(Objects.isNull(role)) {
            return null;
        }
        return RoleDTO.of(role.getId(),
                role.getName(),
                role.getDescription());
    }
}
