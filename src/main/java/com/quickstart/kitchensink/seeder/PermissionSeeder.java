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

                    // Member Permissions
                    Permission.of("MEMBER_CREATE", "Can create members"),
                    Permission.of("MEMBER_DELETE", "Can delete members"),
                    Permission.of("MEMBER_EDIT", "Can edit member details"),
                    Permission.of("MEMBER_VIEW", "Can view home dashboard"),

                    Permission.of("MEMBER_LOCK", "Can lock a member"),
                    Permission.of("MEMBER_EXPIRE_PASSWORD", "Can expire a member's password"),
                    Permission.of("MEMBER_RESET_PASSWORD", "Can reset a member's password")
            );
            permissionRepository.saveAll(permissions);
        }
    }
}
