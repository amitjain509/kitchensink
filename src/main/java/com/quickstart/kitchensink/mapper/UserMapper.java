package com.quickstart.kitchensink.mapper;

import com.quickstart.kitchensink.dto.request.user.UserCreateRequest;
import com.quickstart.kitchensink.dto.request.user.UserUpdateRequest;
import com.quickstart.kitchensink.dto.response.BasicRoleDTO;
import com.quickstart.kitchensink.dto.response.RoleDTO;
import com.quickstart.kitchensink.dto.response.UserDTO;
import com.quickstart.kitchensink.model.Permission;
import com.quickstart.kitchensink.model.Role;
import com.quickstart.kitchensink.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class UserMapper {

    public static UserDTO fromCreateRequest(UserCreateRequest request) {
        return UserDTO.builder()
                .name(request.getName())
                .email(request.getEmail())
                .phoneNumber(request.getPhoneNumber())
                .userType(request.getUserType())
                .roles(List.of(BasicRoleDTO.of(request.getRoleId(), null, null)))
                .build();
    }

    public static UserDTO fromUpdateRequest(UserUpdateRequest request, RoleDTO roleDTO) {
        return UserDTO.builder()
                .roles(List.of(roleDTO))
                .name(request.getName())
                .email(request.getEmail())
                .phoneNumber(request.getPhoneNumber())
                .userType(request.getUserType())
                .build();
    }

    public static UserDTO fromEntity(User user) {
        if (Objects.isNull(user)) {
            return null;
        }
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

    private static List<BasicRoleDTO> toRole(List<Role> roleList) {
        if (CollectionUtils.isEmpty(roleList)) {
            return List.of();
        }
        return Optional.of(roleList.stream()
                .map(RoleMapper::fromEntityToBasicDTO)
                .collect(Collectors.toList())).orElse(List.of());
    }

    private static Set<String> getPermissions(List<Role> roles) {
        if (CollectionUtils.isEmpty(roles)) {
            return Set.of();
        }
        return roles.stream()
                .filter(Objects::nonNull) // Filter out null roles
                .flatMap(role -> Optional.ofNullable(role.getPermissions()).orElse(List.of()).stream()) // Flatten permissions from each role
                .map(Permission::getName) // Extract permission names
                .collect(Collectors.toSet());
    }
}
