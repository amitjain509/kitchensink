package com.quickstart.kitchensink.service;

import com.quickstart.kitchensink.model.Member;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class AdminUserInitializer {

    private final MemberRegistrationService memberRegistrationService;

    @Value("${admin.email}")
    private String adminEmail;

    @Value("${admin.password}")
    private String adminPassword;

    @Bean
    public CommandLineRunner initAdminUser() {
        return args -> {
            if (!memberRegistrationService.isMemberExistByEmailId(adminEmail)) {
                BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
                String hashedPassword = encoder.encode(adminPassword);

                Member admin = Member.builder()
                        .name("Admin User")
                        .email(adminEmail)
                        .password(hashedPassword)
                        .roles(List.of("ADMIN"))
                        .build();

                memberRegistrationService.saveMember(admin);
                log.info("✅ Admin user created.");
            } else {
                log.info("ℹ️ Admin user already exists.");
            }
        };
    }
}
