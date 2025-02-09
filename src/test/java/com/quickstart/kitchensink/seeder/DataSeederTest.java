package com.quickstart.kitchensink.seeder;

import com.quickstart.kitchensink.dto.response.UserDTO;
import com.quickstart.kitchensink.model.Permission;
import com.quickstart.kitchensink.model.Role;
import com.quickstart.kitchensink.model.User;
import com.quickstart.kitchensink.repository.PermissionRepository;
import com.quickstart.kitchensink.repository.RoleRepository;
import com.quickstart.kitchensink.repository.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DataSeederTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private RoleRepository roleRepository;
    @Mock
    private PermissionRepository permissionRepository;
    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private AdminUserSeeder adminUserSeeder;
    @InjectMocks
    private RoleSeeder roleSeeder;
    @InjectMocks
    private PermissionSeeder permissionSeeder;

    private DataSeeder dataSeeder;

    @Captor
    ArgumentCaptor<List<Permission>> permissionCaptor;

    @Captor
    ArgumentCaptor<List<Role>> roleCaptor;

    @Captor
    ArgumentCaptor<User> userCaptor;

    @BeforeEach
    void setup() {
        dataSeeder = Mockito.spy(new DataSeeder(adminUserSeeder, roleSeeder, permissionSeeder));
        lenient().when(permissionRepository.findAll()).thenReturn(List.of(
                Permission.of("ALL", "Grants all permissions"),
                Permission.of("USER_PROFILE_VIEW", "Can view user profile"),
                Permission.of("USER_RESET_PASSWORD", "Can reset a user's password")
        ));
    }

    @Test
    void testDataSeeder_whenDataExists() {
        when(permissionRepository.count()).thenReturn(1L);
        when(roleRepository.count()).thenReturn(1L);
        when(userRepository.findByEmail(null)).thenReturn(Optional.of(User.toEntity(UserDTO.builder().email("").build(), "")));
        when(roleRepository.findByName("ADMIN")).thenReturn(Optional.of(Role.of("ADMIN", "Administrator role", List.of())));

        dataSeeder.run();

        verify(permissionRepository, never()).saveAll(anyCollection());
        verify(roleRepository, never()).saveAll(anyCollection());
        verify(userRepository, never()).save(any());
    }

    @Test
    void testDataSeeder_whenNoDataExists() {
        when(permissionRepository.count()).thenReturn(0L);
        when(roleRepository.count()).thenReturn(0L);
        when(userRepository.findByEmail(null)).thenReturn(Optional.empty());
        when(roleRepository.findByName("ADMIN")).thenReturn(Optional.of(Role.of("ADMIN", "Administrator role", List.of())));

        dataSeeder.run();

        verify(permissionRepository).saveAll(permissionCaptor.capture());
        Assertions.assertEquals(permissionCaptor.getValue().size(), 14);

        verify(roleRepository).saveAll(roleCaptor.capture());
        Assertions.assertEquals(roleCaptor.getValue().size(), 2);

        verify(userRepository).save(userCaptor.capture());
        Assertions.assertNotNull(userCaptor.getValue());
    }
}
