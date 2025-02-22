package com.quickstart.kitchensink.dto.response;

import com.quickstart.kitchensink.enums.UserType;
import com.quickstart.kitchensink.mapper.RoleMapper;
import com.quickstart.kitchensink.model.User;
import lombok.Builder;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Getter
@Builder
public class AuthResponseDTO {
    private String userId;
    private String name;
    private String email;
    private String token;
    private UserType userType;
    private List<RoleDTO> roles;
    private List<String> permissions;
    private boolean isPasswordResetRequired;


    public static AuthResponseDTO from(User user, String token) {
        return AuthResponseDTO.builder()
                .userId(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .token(token)
                .userType(user.getUserType())
                .roles(Optional.ofNullable(user.getRoles()).orElse(List.of()).stream().map(RoleMapper::fromEntity).collect(Collectors.toList()))
                .permissions(Optional.ofNullable(user.getAuthorities()).orElse(List.of()).stream()
                        .map(GrantedAuthority::getAuthority)
                        .collect(Collectors.toList()))
                .isPasswordResetRequired(user.isPasswordResetRequired())
                .build();
    }
}
