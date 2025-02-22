package com.quickstart.kitchensink.mapper;

import com.quickstart.kitchensink.dto.request.role.RoleCreateRequest;
import com.quickstart.kitchensink.dto.response.BasicRoleDTO;
import com.quickstart.kitchensink.dto.response.RoleDTO;
import com.quickstart.kitchensink.model.Role;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Objects;
import java.util.Optional;

public class RoleMapper {

    public static BasicRoleDTO fromRoleCreateRequest(RoleCreateRequest request) {
        return BasicRoleDTO.of(request.getRoleName(), request.getRoleDescription());
    }

    public static BasicRoleDTO fromEntityToBasicDTO(Role role) {
        return BasicRoleDTO.of(role.getId(), role.getName(), role.getDescription());
    }
    public static RoleDTO fromEntity(Role role) {
        return RoleDTO.of(role.getId(),
                role.getName(),
                role.getDescription(),
                new ArrayList<>(Optional.ofNullable(role.getPermissions())
                        .orElse(Collections.emptyList())));
    }

    public static Role toEntity(BasicRoleDTO roleDTO) {
        if (Objects.isNull(roleDTO)) {
            return null;
        }
        return Role.of(roleDTO.getRoleId(), roleDTO.getRoleName(), roleDTO.getRoleDescription());
    }
}
