package com.quickstart.kitchensink.dto.response;

import com.quickstart.kitchensink.enums.UserType;
import com.quickstart.kitchensink.mapper.UserMapper;
import com.quickstart.kitchensink.model.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class AuthResponseDTO {
    private String userId;
    private String name;
    private String email;
    private String token;
    private UserType userType;
    private List<String> permissions;
    private boolean isPasswordResetRequired;


    public static AuthResponseDTO from(User user, String token, List<String> permissions) {
        return AuthResponseDTO.builder()
                .userId(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .token(token)
                .userType(user.getUserType())
                .permissions(permissions)
                .isPasswordResetRequired(user.isPasswordResetRequired())
                .build();
    }
}
