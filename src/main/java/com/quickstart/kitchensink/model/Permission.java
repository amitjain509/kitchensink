package com.quickstart.kitchensink.model;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Builder
@RequiredArgsConstructor
@Document(collection = "permissions")
public class Permission {

    @Id
    private final String id;
    @Indexed(unique = true)
    private final String name; // e.g., "USER_CREATE", "USER_DELETE"
    private final String description; // Detailed description of the permission

    public static Permission of(String name, String description) {
        return Permission.builder()
                .name(name)
                .description(description)
                .build();
    }
}
