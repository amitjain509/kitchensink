package com.quickstart.kitchensink.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class RoleDTO {
    private String roleId;
    private String roleName;
    private String roleDescription;
    private List<String> permissions;

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

    public static RoleDTO of(String id, String name, String description, List<String> permissions) {
        return RoleDTO.builder()
                .roleId(id)
                .roleName(name)
                .roleDescription(description)
                .build();
    }
}
