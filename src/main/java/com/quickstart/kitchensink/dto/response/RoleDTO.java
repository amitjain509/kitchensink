package com.quickstart.kitchensink.dto.response;

import com.quickstart.kitchensink.dto.PermissionDTO;
import com.quickstart.kitchensink.model.Permission;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class RoleDTO extends BasicRoleDTO {
    private List<PermissionDTO> permissions;

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
