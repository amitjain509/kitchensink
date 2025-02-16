package com.quickstart.kitchensink.dto;

import lombok.Builder;

@Builder
public class PermissionDTO {
    private String id;
    private String name;
    private String description;

    public static PermissionDTO of(String id, String name, String description) {
        return PermissionDTO.builder()
                .id(id)
                .name(name)
                .description(description)
                .build();
    }
}
