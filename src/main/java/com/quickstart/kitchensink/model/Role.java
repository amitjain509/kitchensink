package com.quickstart.kitchensink.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Getter
@Builder
@Document(collection = "roles")
public class Role {

    @Id
    private String id;  // Example: "admin", "member"

    @Indexed(unique = true)
    private String name; // Example: "ADMIN", "MEMBER"

    private String description;

    @DBRef
    private List<Permission> permissions; // Example: ["USER_CREATE", "USER_DELETE"]

    public static Role of(String name, String description, List<Permission> permissions) {
        return Role.builder()
                .name(name)
                .description(description)
                .permissions(permissions)
                .build();
    }

    public static Role of(String name, String description) {
        return Role.builder()
                .name(name)
                .description(description)
                .build();
    }

    public static Role of(String roleId, String name, String description) {
        return Role.builder()
                .id(roleId)
                .name(name)
                .description(description)
                .build();
    }

    public void updatePermissions(List<Permission> permissions) {
        this.permissions = permissions;
    }
}
