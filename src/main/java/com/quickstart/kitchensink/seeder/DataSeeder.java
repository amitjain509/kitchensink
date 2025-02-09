package com.quickstart.kitchensink.seeder;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DataSeeder implements CommandLineRunner {
    private final AdminUserSeeder adminUserSeeder;
    private final RoleSeeder roleSeeder;
    private final PermissionSeeder permissionSeeder;

    @Override
    public void run(String... args) {
        permissionSeeder.seedPermissions();
        roleSeeder.seedRoles();
        adminUserSeeder.seedAdminUser();
    }
}
