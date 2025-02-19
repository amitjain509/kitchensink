package com.quickstart.kitchensink.seeder;

import com.quickstart.kitchensink.model.Permission;
import com.quickstart.kitchensink.repository.PermissionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class PermissionSeeder {

    private final PermissionRepository permissionRepository;

    public void seedPermissions() {
        if (permissionRepository.count() == 0) {
            List<Permission> permissions = List.of(
                    Permission.of("ALL", "Grants all permissions"),
                    Permission.of("USER_CREATE", "Can create users"),
                    Permission.of("USER_DELETE", "Can delete users"),
                    Permission.of("USER_EDIT", "Can edit user details"),
                    Permission.of("USER_VIEW", "Can view home dashboard"),
                    Permission.of("USER_LOCK", "Can lock a user"),
                    Permission.of("USER_EXPIRE_PASSWORD", "Can expire a user's password"),

                    Permission.of("USER_PROFILE_EDIT", "Can edit user profile"),
                    Permission.of("USER_PROFILE_VIEW", "Can view user profile"),
                    Permission.of("USER_RESET_PASSWORD", "Can reset a user's password"),

                    Permission.of("ROLE_CREATE", "Can create roles"),
                    Permission.of("ROLE_EDIT", "Can edit role and assign permission to role"),
                    Permission.of("ROLE_VIEW", "Can view roles"),
                    Permission.of("ROLE_DELETE", "Can delete roles")
            );
            permissionRepository.saveAll(permissions);
        }
    }
}
