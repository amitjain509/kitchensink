package com.quickstart.kitchensink.seeder;

import com.quickstart.kitchensink.model.Permission;
import com.quickstart.kitchensink.model.Role;
import com.quickstart.kitchensink.repository.PermissionRepository;
import com.quickstart.kitchensink.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class RoleSeeder {
    private final RoleRepository roleRepository;
    private final PermissionRepository permissionRepository;

    public void seedRoles() {
        if (roleRepository.count() == 0) {
            Map<String, Permission> permissionMap = permissionRepository.findAll().stream()
                    .collect(Collectors.toMap(Permission::getName, p -> p));

            Role adminRole = Role.of("ADMIN", "Administrator role", List.of(permissionMap.get("ALL")));

            Role userRole = Role.of("USER", "User role", List.of(permissionMap.get("MEMBER_CREATE")));

            roleRepository.saveAll(List.of(adminRole, userRole));
        }
    }
}
