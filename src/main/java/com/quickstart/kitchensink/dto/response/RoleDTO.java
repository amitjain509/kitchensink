package com.quickstart.kitchensink.dto.response;

import com.quickstart.kitchensink.dto.PermissionDTO;
import com.quickstart.kitchensink.model.Permission;
import lombok.Builder;
import lombok.Getter;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@Builder
public class RoleDTO {
    private String roleId;
    private String roleName;
    private String roleDescription;
    private List<PermissionDTO> permissions;

    public static RoleDTO of(String name, String description) {
        return RoleDTO.builder()
                .roleName(name)
                .roleDescription(description)
                .build();
    }

    public static RoleDTO of(String id, String name, String description) {
        return RoleDTO.builder()
                .roleId(id)
                .roleName(name)
                .roleDescription(description)
                .build();
    }

    public static RoleDTO of(String id, String name, String description, List<Permission> permissions) {
        return RoleDTO.builder()
                .roleId(id)
                .roleName(name)
                .roleDescription(description)
                .permissions(permissions.stream()
                        .map(p -> PermissionDTO.of(p.getId(), p.getName(), p.getDescription()))
                        .collect(Collectors.toList()))
                .build();
    }
}
