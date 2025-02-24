package com.quickstart.kitchensink.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class BasicRoleDTO {
    private String roleId;
    private String roleName;
    private String roleDescription;


    public static BasicRoleDTO of(String name, String description) {
        return BasicRoleDTO.builder()
                .roleName(name)
                .roleDescription(description)
                .build();
    }

    public static BasicRoleDTO of(String id, String name, String description) {
        return BasicRoleDTO.builder()
                .roleId(id)
                .roleName(name)
                .roleDescription(description)
                .build();
    }
}
