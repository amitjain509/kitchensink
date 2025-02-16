package com.quickstart.kitchensink.mapper;

import com.quickstart.kitchensink.dto.PasswordDTO;
import com.quickstart.kitchensink.dto.request.user.UserUpdateRequest;
import com.quickstart.kitchensink.dto.response.UserDTO;
import com.quickstart.kitchensink.dto.request.user.PasswordResetRequest;
import com.quickstart.kitchensink.dto.request.user.UserCreateRequest;
import com.quickstart.kitchensink.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserMapper {
    private final PasswordEncoder passwordEncoder;

    public UserDTO fromCreateRequest(UserCreateRequest request) {
        return UserDTO.builder()
                .name(request.getName())
                .email(request.getEmail())
                .phoneNumber(request.getPhoneNumber())
                .password(passwordEncoder.encode(request.getPassword()))
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
                .build();
    }
}
