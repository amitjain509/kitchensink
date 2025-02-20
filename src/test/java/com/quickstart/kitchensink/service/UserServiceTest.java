package com.quickstart.kitchensink.service;

import com.quickstart.kitchensink.dto.PasswordDTO;
import com.quickstart.kitchensink.dto.response.UserDTO;
import com.quickstart.kitchensink.enums.UserType;
import com.quickstart.kitchensink.model.Role;
import com.quickstart.kitchensink.model.User;
import com.quickstart.kitchensink.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private RoleService roleService;

    @InjectMocks
    private UserService userService;

    private User user;
    private UserDTO userDTO;

    @BeforeEach
    void setUp() {
        userDTO = UserDTO.builder().userId("1").email("test@example.com").userType(UserType.USER)
                .phoneNumber("9876543210").build();
        user = User.toEntity(userDTO);
    }

    @Test
    void createUser_ShouldReturnUserDTO_WhenUserIsCreatedSuccessfully() {
        when(userRepository.existsByEmail(userDTO.getEmail())).thenReturn(false);
        when(userRepository.save(any(User.class))).thenReturn(user);
        UserDTO createdUser = userService.createUser(userDTO);

        assertThat(createdUser).isNotNull();
        assertThat(createdUser.getEmail()).isEqualTo(userDTO.getEmail());
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void createUser_ShouldThrowException_WhenEmailAlreadyExists() {
        when(userRepository.existsByEmail(userDTO.getEmail())).thenReturn(true);

        assertThatThrownBy(() -> userService.createUser(userDTO))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Email already exists");

        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void updateUser_ShouldUpdateUserSuccessfully() {
        when(userRepository.findByEmail(userDTO.getEmail())).thenReturn(Optional.of(user));

        userService.updateUser(userDTO);

        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void updateUser_ShouldThrowException_WhenUserNotFound() {
        when(userRepository.findByEmail(userDTO.getEmail())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> userService.updateUser(userDTO))
                .isInstanceOf(UsernameNotFoundException.class)
                .hasMessage("User does not exists");

        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void deleteUser_ShouldDeleteUserSuccessfully() {
        doNothing().when(userRepository).deleteById("1");

        userService.deleteUser("1");

        verify(userRepository, times(1)).deleteById("1");
    }

    @Test
    void resetPassword_ShouldUpdatePasswordSuccessfully() {
        when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.of(user));

        userService.updatePassword(user.getEmail(), "newPassword");

        verify(userRepository, times(1)).save(user);
    }

    @Test
    void resetPassword_ShouldThrowException_WhenUserNotFound() {
        when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> userService.updatePassword(user.getEmail(), "newPassword"))
                .isInstanceOf(UsernameNotFoundException.class)
                .hasMessage("User does not exists");

        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void lockUser_ShouldLockUserSuccessfully() {
        when(userRepository.findById("1")).thenReturn(Optional.of(user));

        userService.lockUser("1");

        verify(userRepository, times(1)).save(user);
    }

    @Test
    void lockUser_ShouldDoNothing_WhenUserNotFound() {
        when(userRepository.findById("1")).thenReturn(Optional.empty());

        userService.lockUser("1");

        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void unlockUser_ShouldUnlockUserSuccessfully() {
        when(userRepository.findById("1")).thenReturn(Optional.of(user));

        userService.unlockUser("1");

        verify(userRepository, times(1)).save(user);
    }

    @Test
    void unlockUser_ShouldDoNothing_WhenUserNotFound() {
        when(userRepository.findById("1")).thenReturn(Optional.empty());

        userService.unlockUser("1");

        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void updatePassword_ShouldUpdatePasswordSuccessfully() {
        PasswordDTO passwordDTO = PasswordDTO.of("oldPassword", "newPassword");
        when(userRepository.findByEmailAndPassword(user.getEmail(), passwordDTO.getOldPassword()))
                .thenReturn(Optional.of(user));

        userService.updatePassword(user.getEmail(), passwordDTO);

        verify(userRepository, times(1)).save(user);
    }

    @Test
    void updatePassword_ShouldThrowException_WhenCredentialsAreInvalid() {
        PasswordDTO passwordDTO = PasswordDTO.of("wrongOldPassword", "newPassword");
        when(userRepository.findByEmailAndPassword(user.getEmail(), passwordDTO.getOldPassword()))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() -> userService.updatePassword(user.getEmail(), passwordDTO))
                .isInstanceOf(AuthenticationCredentialsNotFoundException.class)
                .hasMessage("Invalid email or password");

        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void assignRolesToUser_ShouldAssignRolesSuccessfully() {
        Role role = Role.of("ROLE_USER", "");
        List<String> roleNames = List.of("ROLE_USER");
        List<Role> roles = List.of(role);

        when(roleService.validateAndGetRoles(roleNames)).thenReturn(roles);
        when(userRepository.findById(any())).thenReturn(Optional.of(user));

        userService.assignRolesToUser(user.getEmail(), roleNames);

        verify(userRepository, times(1)).save(user);
    }

    @Test
    void assignRolesToUser_ShouldThrowException_WhenUserNotFound() {
        List<String> roleNames = List.of("ROLE_USER");

        assertThatThrownBy(() -> userService.assignRolesToUser(user.getEmail(), roleNames))
                .isInstanceOf(UsernameNotFoundException.class)
                .hasMessage("User does not exist");

        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void isMemberExistByEmailId_ShouldReturnTrue_WhenUserExists() {
        when(userRepository.existsByEmail(user.getEmail())).thenReturn(true);

        boolean exists = userService.isMemberExistByEmailId(user.getEmail());

        assertThat(exists).isTrue();
    }

    @Test
    void isMemberExistByEmailId_ShouldReturnFalse_WhenUserDoesNotExist() {
        when(userRepository.existsByEmail(user.getEmail())).thenReturn(false);

        boolean exists = userService.isMemberExistByEmailId(user.getEmail());

        assertThat(exists).isFalse();
    }

    @Test
    void findAllUsersByUserType_ShouldReturnUsers_WhenUsersExist() {
        when(userRepository.findByUserType(UserType.USER)).thenReturn(List.of(user));

        List<UserDTO> users = userService.findAllUsersByUserType(UserType.USER);

        assertThat(users).isNotEmpty();
        assertThat(users).hasSize(1);
    }
}
