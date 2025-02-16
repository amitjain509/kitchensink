package com.quickstart.kitchensink.seeder;

import com.quickstart.kitchensink.enums.UserType;
import com.quickstart.kitchensink.model.Role;
import com.quickstart.kitchensink.model.User;
import com.quickstart.kitchensink.repository.RoleRepository;
import com.quickstart.kitchensink.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class AdminUserSeeder {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;

    @Value("${admin.email}")
    private String adminEmail;

    @Value("${admin.password}")
    private String adminPassword;

    public void seedAdminUser() {
        Optional<User> existingAdmin = userRepository.findByEmail(adminEmail);
        Optional<Role> role = roleRepository.findByName("ADMIN");
        if (existingAdmin.isEmpty() && role.isPresent()) {
            User adminUser = User.builder()
                    .email(adminEmail)
                    .password(passwordEncoder.encode(adminPassword))
                    .roles(List.of(role.get()))
                    .active(true)
                    .userType(UserType.ADMIN)
                    .isPasswordResetRequired(false)
                    .build();

            userRepository.save(adminUser);
            System.out.println("✅ Admin user seeded successfully!");
        } else {
            System.out.println("⚠️ Admin user already exists, skipping...");
        }
    }
}
