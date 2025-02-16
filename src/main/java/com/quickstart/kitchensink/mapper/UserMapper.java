package com.quickstart.kitchensink.mapper;

import com.quickstart.kitchensink.dto.PasswordDTO;
import com.quickstart.kitchensink.dto.request.user.PasswordResetRequest;
import com.quickstart.kitchensink.dto.request.user.UserCreateRequest;
import com.quickstart.kitchensink.dto.request.user.UserUpdateRequest;
import com.quickstart.kitchensink.dto.response.RoleDTO;
import com.quickstart.kitchensink.dto.response.UserDTO;
import com.quickstart.kitchensink.model.Permission;
import com.quickstart.kitchensink.model.Role;
import com.quickstart.kitchensink.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class UserMapper {
    private final PasswordEncoder passwordEncoder;
    @Value("${default.password}")
    private String defaultPassword;

    public UserDTO fromCreateRequest(UserCreateRequest request) {
        return UserDTO.builder()
                .name(request.getName())
                .email(request.getEmail())
                .phoneNumber(request.getPhoneNumber())
                .password(passwordEncoder.encode(defaultPassword))
                .userType(request.getUserType())
                .build();
    }

    public UserDTO fromUpdateRequest(UserUpdateRequest request) {
        return UserDTO.builder()
                .name(request.getName())
                .email(request.getEmail())
                .phoneNumber(request.getPhoneNumber())
                .userType(request.getUserType())
                .build();
    }

    public PasswordDTO fromPasswordUpdateRequest(PasswordResetRequest request) {
        return PasswordDTO.of(passwordEncoder.encode(request.getOldPassword()),
                passwordEncoder.encode(request.getNewPassword()));
    }

    public static UserDTO fromEntity(User user) {
        return UserDTO.builder()
                .userId(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .phoneNumber(user.getPhoneNumber())
                .userType(user.getUserType())
                .active(user.isActive())
                .locked(user.isLocked())
                .roles(toRole(user.getRoles()))
                .isPasswordResetRequired(user.isPasswordResetRequired())
                .permissions(getPermissions(user.getRoles()))
                .build();
    }

    private static List<RoleDTO> toRole(List<Role> roleList) {
        if (CollectionUtils.isEmpty(roleList)) {
            return List.of();
        }
        return Optional.of(roleList.stream()
                .map(RoleMapper::fromEntityWithoutPermission)
                .collect(Collectors.toList())).orElse(List.of());
    }

    private static Set<String> getPermissions(List<Role> roles) {
        if (CollectionUtils.isEmpty(roles)) {
            return Set.of();
        }
        return roles.stream()
                .filter(Objects::nonNull) // Filter out null roles
                .flatMap(role -> role.getPermissions().stream()) // Flatten permissions from each role
                .map(Permission::getName) // Extract permission names
                .collect(Collectors.toSet());
    }
}
