package com.quickstart.kitchensink.model;

import com.quickstart.kitchensink.dto.response.UserDTO;
import com.quickstart.kitchensink.enums.UserType;
import lombok.Builder;
import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.CollectionUtils;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Getter
@Builder
@Document(collection = "users")
public class User implements UserDetails {

    @Id
    private String id;

    private String name;

    @Indexed(unique = true)
    private String email;

    private String phoneNumber;

    private String password;

    private boolean active;

    private boolean locked;

    private boolean isPasswordResetRequired;

    @Indexed
    @Field("user_type")
    private UserType userType;

    @DBRef
    private List<Role> roles;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        if (CollectionUtils.isEmpty(roles)) {
            return List.of();
        }
        return roles.stream()
                .flatMap(role -> Optional.ofNullable(role.getPermissions()).orElse(List.of()).stream())
                .map(permission -> new org.springframework.security.core.authority.SimpleGrantedAuthority(permission.getName()))
                .collect(Collectors.toList());
    }

    @Override
    public String getUsername() {
        return this.email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return !locked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return active;
    }

    public static User toEntity(UserDTO userDTO) {
        return User.builder()
                .name(userDTO.getName())
                .email(userDTO.getEmail())
                .phoneNumber(userDTO.getPhoneNumber())
                .password(userDTO.getPassword())
                .active(true)
                .locked(false)
                .isPasswordResetRequired(true)
                .userType(userDTO.getUserType())
                .build();
    }

    public void lockUser() {
        this.active = false;
        this.locked = true;
    }

    public void unlockUser() {
        this.active = true;
        this.locked = false;
    }

    public void updateUserDetails(UserDTO userDTO) {
        this.name = userDTO.getName();
        this.phoneNumber = userDTO.getPhoneNumber();
    }

    public void updatePassword(String password) {
        this.password = password;
        this.isPasswordResetRequired = false;
        this.active = true;
    }

    public void updateRoles(List<Role> roles) {
        this.roles = roles;
    }
}
